"""检查 mod 物品的可获得性（配方闭包推导）。

思路：
  1. 从 ModItems.java / ModBlocks.java 收集本 mod 注册的全部物品 id；
  2. 收集所有"产出源"：
       - data/mitenewworld/recipe/**  （含自定义 mod_crafting_shaped / modfurnace）
       - data/mitenewworld/loot_table/**  （挖掘掉落，视为基础可得）
       - Java: ModCastingTemplates 的铸造模板产出
       - Java: 合金炉（粗矿 / 合金 / 废金属 -> 锭 / 粒 / 废金属）
  3. 以"原版物品 + 战利品掉落"为种子，沿配方前向迭代到不动点；
  4. 仍不在集合里的，即没有任何获得途径的物品。

用法：  python tools/check_unobtainable.py [--md 输出报告.md]
"""
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
NS = 'mitenewworld'
JAVA = ROOT / 'src' / 'main' / 'java'
RES = ROOT / 'src' / 'main' / 'resources'
ASSETS = RES / 'assets' / NS


def read(path):
    try:
        return path.read_text(encoding='utf-8')
    except Exception:
        return ''


# ============================================================
#  1. 物品清单
# ============================================================

def load_items():
    """返回 (id -> java常量名, 方块id -> 常量名, 常量名 -> id)"""
    items = {}      # id -> const
    const2id = {}   # const -> id

    # 所有出现过 ModItems.register( 的注册类（排除 cover 包：那是原版物品的包装）
    reg_files = [p for p in JAVA.rglob('*.java')
                 if 'cover' not in p.parts and 'ModItems.register(' in read(p)]
    for f in reg_files:
        text = read(f)
        # public static final Item NAME = ModItems.register(\n "id",   /  同一行写法
        for m in re.finditer(r'public static final Item (\w+)\s*=\s*(?:ModItems\.)?register\(\s*"([a-z0-9_]+)"', text):
            const2id[m.group(1)] = m.group(2)
            items[m.group(2)] = m.group(1)
        # registerRawOre("copper", ...) -> raw_copper
        for m in re.finditer(r'public static final Item (\w+)\s*=\s*registerRawOre\(\s*"([a-z0-9_]+)"', text):
            iid = 'raw_' + m.group(2)
            const2id[m.group(1)] = iid
            items[iid] = m.group(1)

    blocks = {}
    btext = read(JAVA / 'com' / 'mitenewworld' / 'registry' / 'ModBlocks.java')
    for m in re.finditer(r'public static final Block (\w+)\s*=\s*register\(\s*"([a-z0-9_]+)"', btext):
        blocks[m.group(2)] = m.group(1)
    return items, blocks, const2id


# ============================================================
#  2. 标签
# ============================================================

def load_tags():
    tags = {}
    tag_dir = RES / 'data' / NS / 'tags'
    for p in tag_dir.rglob('*.json'):
        try:
            data = json.loads(read(p))
        except Exception:
            continue
        rel = p.relative_to(tag_dir)
        key = '#%s:%s' % (NS, rel.with_suffix('').as_posix())
        tags[key] = data.get('values', [])
    return tags


# ============================================================
#  3. 配方
# ============================================================

ING_FIELDS = ('ingredient', 'ingredients', 'input', 'base', 'addition', 'template')
SHAPED_TYPES = ('mod_crafting_shaped', 'crafting_shaped')


def collect_ing(value, out):
    """把任意形态的"食材"写法收集成 (kind, id) 集合。"""
    if value is None:
        return
    if isinstance(value, str):
        if value.startswith('#'):
            out.add(('tag', value[1:]))
        elif value:
            out.add(('item', value))
    elif isinstance(value, list):
        for e in value:
            collect_ing(e, out)
    elif isinstance(value, dict):
        if 'item' in value:
            out.add(('item', value['item']))
        if 'tag' in value:
            out.add(('tag', value['tag']))
        for f in ING_FIELDS:
            if f in value:
                collect_ing(value[f], out)


def result_ids(value):
    out = set()
    if isinstance(value, str):
        out.add(value)
    elif isinstance(value, dict):
        if 'id' in value:
            out.add(value['id'])
    elif isinstance(value, list):
        for e in value:
            out |= result_ids(e)
    return {r for r in out if r}


def parse_recipe(data):
    """-> (outputs:set[id], inputs:set[(kind,id)], ok:bool)"""
    rtype = str(data.get('type', '')).split(':')[-1]
    outputs = result_ids(data.get('result', data.get('results')))
    inputs = set()

    if rtype in SHAPED_TYPES:
        pattern = data.get('pattern', []) or []
        used = {ch for row in pattern for ch in row if ch != ' '}
        key = data.get('key', {}) or {}
        for ch, v in key.items():
            if ch in used:
                collect_ing(v, inputs)
    else:
        for f in ING_FIELDS:
            if f in data:
                collect_ing(data[f], inputs)
        # 兜底：shaped 家族的 key
        if 'key' in data and isinstance(data['key'], dict):
            collect_ing(list(data['key'].values()), inputs)
    return outputs, inputs


def load_recipes():
    """-> list[(文件路径, outputs, inputs)]"""
    out = []
    rdir = RES / 'data' / NS / 'recipe'
    if not rdir.exists():
        return out
    for p in sorted(rdir.rglob('*.json')):
        try:
            data = json.loads(read(p))
        except Exception as e:
            print('[警告] 配方 JSON 解析失败: %s (%s)' % (p, e))
            continue
        outputs, inputs = parse_recipe(data)
        out.append((p, outputs, inputs))
    return out


# ============================================================
#  4. 战利品表
# ============================================================

def load_loot_items():
    """战利品表产出。除了本 mod 命名空间，还要扫 data/minecraft/loot_table —— 项目在这里
    覆写了原版沙砾等掉落表，合金粒 / 燧石碎片 / 玻璃碎片就是从沙砾掉的。"""
    found = set()
    ldirs = [RES / 'data' / NS / 'loot_table', RES / 'data' / 'minecraft' / 'loot_table']

    def walk(node):
        if isinstance(node, dict):
            name = node.get('name')
            if isinstance(name, str) and name.startswith(NS + ':'):
                found.add(name)
            for v in node.values():
                walk(v)
        elif isinstance(node, list):
            for v in node:
                walk(v)

    for ldir in ldirs:
        if not ldir.exists():
            continue
        for p in sorted(ldir.rglob('*.json')):
            try:
                walk(json.loads(read(p)))
            except Exception:
                continue
    return found


def load_code_produced(const2id):
    """代码直接产出的物品：右键交互（空碗右键牛 -> 碗装牛奶、金属桶右键牛 -> 牛奶桶、
    空桶右键流体 -> 水桶/岩浆桶/石桶），没有配方也没有战利品表。
    扫整个源码里的 new ItemStack(ModItems./ModFoodItems./ModBlocks.X)。"""
    found = set()
    src_dir = JAVA / 'com' / 'mitenewworld'
    if not src_dir.exists():
        return found
    for p in src_dir.rglob('*.java'):
        for m in re.finditer(r'new ItemStack\((?:ModItems|ModFoodItems|ModBlocks)\.(\w+)', read(p)):
            item_id = const2id.get(m.group(1))
            if item_id:
                found.add(item_id)

    # 怪物自然生成时持有的装备（MobEquipment 的候选工具/武器清单），
    # 走的是 Item 常量列表而不是 new ItemStack(...)，需要单独扫。
    entity_dir = src_dir / 'entity'
    if entity_dir.exists():
        for p in entity_dir.rglob('*.java'):
            for m in re.finditer(r'ModItems\.(\w+)', read(p)):
                item_id = const2id.get(m.group(1))
                if item_id:
                    found.add(item_id)

    # FILL_IN_WATER：玩家进入游泳姿态时背包里的物品被整体替换
    # （空桶 -> 水桶、岩浆桶 -> 石桶、碗 -> 水碗）。产物是 put 的第二个常量。
    for p in src_dir.rglob('*.java'):
        for m in re.finditer(r'FILL_IN_WATER\.put\(\s*[\w.]+\s*,\s*(?:ModItems\.)?(\w+)\s*\)', read(p)):
            item_id = const2id.get(m.group(1))
            if item_id:
                found.add(item_id)
    return found


# ============================================================
#  5. Java 侧产出：铸造模板 + 合金炉
# ============================================================

SLOT_ITEM = {'INGOT': 'alloy_ingot', 'NUGGET': 'alloy_nugget',
             'BLOCK': 'alloy_block', 'CHAIN': 'alloy_chain'}
HAMMERS = ('alloy_casting_hammer', 'flint_forging_hammer')

# 产物按"主导金属 / 最大含量金属"动态决定的模板：
# 静态 head 只能读到 output 字段那一个 Item，实际能产出整族物品
DYNAMIC_TEMPLATES = {
    'toolbox': ('suffix', '_TOOLBOX'),   # ModItems.toolboxFor(dominant) -> 13 种金属工具箱
    'bucket': ('names', (                # ModItems.bucketFor(dominant) -> 7 种金属空桶
        'COPPER_BUCKET', 'SILVER_BUCKET', 'GOLD_BUCKET', 'IRON_BUCKET',
        'MITHRIL_BUCKET', 'ANCIENT_METAL_BUCKET', 'ADAMANTIUM_BUCKET')),
}


def load_casting_templates(const2id):
    """-> list[(模板id, 输出物品id, 需求物品id集合)]"""
    text = read(JAVA / 'com' / 'mitenewworld' / 'recipe' / 'casting' / 'ModCastingTemplates.java')
    starts = [m.start() for m in re.finditer(r'new CastingTemplate\(', text)]
    res = []
    for i, s in enumerate(starts):
        chunk = text[s: starts[i + 1] if i + 1 < len(starts) else len(text)]
        head = re.match(r'new CastingTemplate\(\s*"([a-z0-9_]+)",\s*ModItems\.(\w+)', chunk)
        if not head:
            continue
        tid, const = head.group(1), head.group(2)
        out_id = const2id.get(const, '??' + const)
        need = {SLOT_ITEM[t] for t in re.findall(r'SlotType\.(\w+)', chunk) if t in SLOT_ITEM}
        # 皮革绳 / 木棍这类辅材也计入需求（原版物品必须带 minecraft: 前缀，
        # 否则 ns() 会误加成本 mod 命名空间）
        for extra, item in (('SINEW', 'sinew'), ('HANDLE', 'minecraft:stick')):
            if 'SlotType.' + extra in chunk:
                need.add(item)
        dyn = DYNAMIC_TEMPLATES.get(tid)
        if dyn and dyn[0] == 'names':
            outs = [const2id[k] for k in dyn[1] if k in const2id]
        elif dyn:
            outs = sorted(v for k, v in const2id.items() if k.endswith(dyn[1]))
        else:
            outs = [out_id]
        for out in outs:
            res.append((tid, out, need))
    return res


def load_java_refs(const2id, block2id):
    """扫描 java 源码里 ModItems.X / ModBlocks.X 的引用位置（判断物品是否"活着"）。"""
    refs = {}
    name2id = dict(const2id)
    name2id.update(block2id)
    for p in JAVA.rglob('*.java'):
        text = read(p)
        if not text:
            continue
        rel = p.relative_to(JAVA).as_posix()
        for ln, line in enumerate(text.splitlines(), 1):
            for m in re.finditer(r'Mod(?:Items|Blocks)\.([A-Z0-9_]+)', line):
                iid = name2id.get(m.group(1))
                if iid:
                    refs.setdefault(iid, []).append('%s:%d' % (rel, ln))
    return refs


def load_world_blocks():
    """世界里天然存在的方块：有方块战利品表，或出现在 worldgen 中。"""
    found = set()
    ldir = RES / 'data' / NS / 'loot_table' / 'blocks'
    if ldir.exists():
        for p in ldir.rglob('*.json'):
            found.add(p.stem)
    # 本 mod 命名空间 + 原版命名空间（有些矿脉是覆盖 data/minecraft/worldgen 的，
    # 例如 ore_nether_gold.json 里放的是 mitenewworld:gold_ore_netherrack）
    for wdir in (RES / 'data' / NS / 'worldgen', RES / 'data' / 'minecraft' / 'worldgen'):
        if not wdir.exists():
            continue
        for p in wdir.rglob('*.json'):
            for m in re.finditer(r'"%s:([a-z0-9_]+)"' % NS, read(p)):
                found.add(m.group(1))
    return found


def load_ore_drop_metals():
    """矿石掉落是代码实现的：ModOreBlock.onStacksDropped 直接掉 RawOreItem，
    不走战利品表，所以静态扫 loot_table 会漏掉全部 raw_* 粗矿。"""
    path = JAVA / 'com' / 'mitenewworld' / 'block' / 'ModOres.java'
    if not path.exists():
        return set()
    text = read(path)
    return set(re.findall(r'case "([a-z_]+)" -> ModItems\.RAW_', text))


def load_furnace_rule(items):
    """合金炉：任意粗矿 / 合金 / 废金属 -> 锭 / 粒 / 废金属"""
    feed = {i for i in items if i.startswith('raw_')} | {'scrap_metal'}
    feed |= {i for i in items if i.startswith('alloy_')}
    out = {'alloy_ingot', 'alloy_nugget', 'scrap_metal'}
    return feed, out


# ============================================================
#  6. 闭包
# ============================================================

def ns(iid):
    return iid if ':' in iid else NS + ':' + iid


def main():
    md_path = None
    if '--md' in sys.argv:
        md_path = sys.argv[sys.argv.index('--md') + 1]

    items, blocks, const2id = load_items()
    tags = load_tags()
    recipes = load_recipes()
    loot = load_loot_items()
    code_produced = load_code_produced(const2id)
    templates = load_casting_templates(const2id)
    furnace_feed, furnace_out = load_furnace_rule(items)
    world_blocks = load_world_blocks()
    ore_metals = load_ore_drop_metals()
    java_refs = load_java_refs(const2id, {v: k for k, v in blocks.items()})

    # 方块对应的物品（有 items/*.json 声明的才算物品形式）
    block_items = {b for b in blocks if (ASSETS / 'items' / (b + '.json')).exists()}

    all_ids = set(items) | block_items

    # ---- 直接产出源统计 ----
    produced = {}   # id -> set(来源说明)
    for p, outputs, _ in recipes:
        for o in outputs:
            if o.startswith(NS + ':'):
                produced.setdefault(o, set()).add('recipe:%s' % p.relative_to(RES).as_posix())
    for l in loot:
        produced.setdefault(l, set()).add('loot')
    # 代码直产（右键牛挤奶等）
    for c in sorted(code_produced):
        produced.setdefault(ns(c), set()).add('code(Java直产/怪物持有)')
    for tid, out_id, _ in templates:
        produced.setdefault(ns(out_id), set()).add('casting:%s' % tid)
    for o in furnace_out:
        produced.setdefault(ns(o), set()).add('alloyfurnace')
    for b in world_blocks:
        if b in all_ids:
            produced.setdefault(ns(b), set()).add('world/loot')
    # 矿石方块在代码里直接掉粗矿（ModOreBlock.onStacksDropped）
    for m in sorted(ore_metals):
        rid = 'raw_' + m
        if rid in all_ids:
            produced.setdefault(ns(rid), set()).add('ore_drop(%s)' % m)

    # ---- 闭包 ----
    def tag_ok(tag):
        if not tag.startswith(NS + ':'):
            return True          # 原版标签视为可满足
        seen = set()

        def expand(t):
            if t in seen or not t.startswith(NS + ':'):
                return set()
            seen.add(t)
            out = set()
            for v in tags.get(t, []):
                if isinstance(v, str):
                    if v.startswith('#'):
                        out |= expand(v)
                    else:
                        out.add(v)
                elif isinstance(v, dict):
                    if 'id' in v:
                        out.add(v['id'])
                    elif 'tag' in v:
                        out |= expand('#' + v['tag'] if ':' in v['tag'] else '#%s:%s' % (NS, v['tag']))
            return out
        return any(m in have for m in expand('#' + tag if not tag.startswith('#') else tag))

    # 种子：战利品掉落 + 世界里天然存在的方块（矿石、原木等）
    # + 矿石方块代码直掉的粗矿（矿脉由 mixin 注入原版 ore feature，worldgen json 里查不到）
    have = set(loot) | {ns(b) for b in world_blocks if b in all_ids}
    have |= {ns(c) for c in code_produced}
    have |= {ns('raw_' + m) for m in ore_metals if 'raw_' + m in all_ids}
    changed = True
    rounds = 0
    while changed and rounds < 200:
        rounds += 1
        changed = False

        def have_item(i):
            """原版物品视为天然可得"""
            return (not i.startswith(NS + ':')) or (i in have)

        # 配方
        for p, outputs, inputs in recipes:
            if not any(o.startswith(NS + ':') for o in outputs):
                continue
            if all(have_item(i) if k == 'item' else tag_ok(i) for k, i in inputs):
                for o in outputs:
                    if o not in have:
                        have.add(o)
                        changed = True

        # 铸造模板（需要铸造台 + 锤子 + 对应形态材料）
        hammer_ok = any(ns(h) in have for h in HAMMERS)
        table_ok = any(ns(t) in have for t in ('mod_casting_table', 'mod_flint_casting_table'))

        def need_ok(n):
            """模板原料：原版物品（木棍等）视为天然可得，本 mod 物品必须在闭包里"""
            i = ns(n)
            return (not i.startswith(NS + ':')) or (i in have)

        for tid, out_id, need in templates:
            if ns(out_id) in have:
                continue
            if table_ok and hammer_ok and all(need_ok(n) for n in need):
                have.add(ns(out_id))
                changed = True

        # 合金炉
        if not furnace_out <= have:
            if any(ns(f) in have for f in furnace_feed):
                for o in furnace_out:
                    if o not in have:
                        have.add(ns(o))
                        changed = True

    # ---- 结论 ----
    def short(iid):
        return iid.split(':')[-1]

    reachable = {short(i) for i in have if i.startswith(NS + ':')}
    no_source = sorted(i for i in all_ids if ns(i) not in produced)
    unreachable = sorted(i for i in all_ids if i not in reachable)

    # 有产出源但因原料缺口而推不出来的：给出缺失原料
    stuck = []
    for i in sorted(set(no_source) & set(unreachable)):
        stuck.append(i)

    unrec = set(unreachable)
    blocked = {}
    for p, outputs, inputs in recipes:
        for o in outputs:
            sid = o.split(':')[-1]
            if sid not in unrec:
                continue
            miss = blocked.setdefault(sid, set())
            for k, v in inputs:
                if k == 'item' and v.startswith(NS + ':') and v not in have:
                    miss.add(v.split(':')[-1])
                elif k == 'tag' and v.startswith(NS + ':') and not tag_ok(v):
                    miss.add('#' + v.split(':')[-1])

    hammer_ok = any(ns(h) in have for h in HAMMERS)
    table_ok = any(ns(t) in have for t in ('mod_casting_table', 'mod_flint_casting_table'))
    for tid, out_id, need in templates:
        if out_id in unrec:
            miss = blocked.setdefault(out_id, set())
            if not table_ok:
                miss.add('铸造台')
            if not hammer_ok:
                miss.add('铸造锤')

            def _need_missing(n):
                i = ns(n)
                return i.startswith(NS + ':') and i not in have

            for n in need:
                if _need_missing(n):
                    miss.add(n)
    for o in furnace_out:
        if o in unrec and not any(ns(f) in have for f in furnace_feed):
            blocked.setdefault(o, set()).add('合金炉原料(粗矿/合金/废金属)')
    blocked = sorted((i, sorted(m)) for i, m in blocked.items())

    def classify(i):
        if 'bucket' in i:
            return '桶类（空桶 / 水桶 / 岩浆桶 / 石桶）'
        if i.endswith(('_input_port', '_output_port', '_smelting_core')) or i.endswith('_brick'):
            return '熔炉结构方块（砖块 / 进出口 / 炉心）'
        if i.endswith('_fragment'):
            return '碎片类（fragment）'
        if i.startswith('raw_'):
            return '粗矿（raw_）'
        if i.startswith('rusted_iron_'):
            return '锈蚀铁工具'
        if i.startswith('alloy_'):
            return '合金装备 / 材料'
        return '其它'

    def grouped_print(seq):
        buckets = {}
        for i in seq:
            buckets.setdefault(classify(i), []).append(i)
        for cat in sorted(buckets, key=lambda c: (-len(buckets[c]), c)):
            w('**%s（%d）**' % (cat, len(buckets[cat])))
            w('')
            for i in buckets[cat]:
                w('  - `%s`' % i)
            w('')

    lines = []
    w = lines.append
    w('# 物品可获得性检查报告')
    w('')
    w('物品总数（ModItems 注册 + 有物品形式的方块）: **%d**' % len(all_ids))
    w('其中方块物品: %d' % len(block_items))
    w('配方文件: %d，铸造模板: %d，战利品掉落(本 mod): %d' % (len(recipes), len(templates), len(loot)))
    w('闭包迭代轮数: %d，推导出的本 mod 物品: %d' % (rounds, len(reachable)))
    w('')
    w('## 一、完全没有任何产出源的物品（%d）' % len(no_source))
    w('')
    w('既不出现在任何配方 result，也不是战利品掉落 / 铸造模板产出 / 合金炉产出。')
    w('')
    cnt = {}
    for i in no_source:
        cnt[classify(i)] = cnt.get(classify(i), 0) + 1
    if cnt:
        w('### 类别概览')
        w('')
        for c, n in sorted(cnt.items(), key=lambda x: -x[1]):
            w('- %s：%d' % (c, n))
        w('')

    if no_source:
        dead = [i for i in no_source if not java_refs.get(i)]
        alive = [i for i in no_source if java_refs.get(i)]
        w('### 1a. 配方/掉落都没有，**Java 代码里也从未引用**（%d）—— 真·孤儿物品' % len(dead))
        w('')
        for i in dead:
            w('- `%s`' % i)
        w('')
        w('### 1b. 无配方产出，但 Java 代码有引用（%d）—— 多半由代码逻辑产出，需人工确认' % len(alive))
        w('')
        for i in alive:
            spots = java_refs[i][:3]
            more = '' if len(java_refs[i]) <= 3 else ' 等 %d 处' % len(java_refs[i])
            w('- `%s` ← %s%s' % (i, ', '.join(spots), more))
    else:
        w('- 无')
    w('')
    w('## 二、配方闭包内无法推导出的物品（%d）' % len(unreachable))
    w('')
    if unreachable:
        grouped_print(unreachable)
    else:
        w('- 无')
    w('')
    w('## 三、有产出源但原料缺口导致拿不到（%d）' % len(blocked))
    w('')
    w('这些物品有配方产出，但配方的某些原料本身不可获得（或只能通过不可获得物合成）。')
    w('')
    if blocked:
        for i, miss in blocked:
            w('- `%s` ← 缺: %s' % (i, ', '.join('`%s`' % m for m in miss) if miss else '(条件未满足)'))
    else:
        w('- 无')
    w('')

    # 反向：配方/战利品引用了未注册的 mod 物品
    known = {ns(i) for i in all_ids}
    dangling = set()
    for p, outputs, inputs in recipes:
        for o in outputs:
            if o.startswith(NS + ':') and o not in known:
                dangling.add((o, 'result', p))
        for k, v in inputs:
            if k == 'item' and v.startswith(NS + ':') and v not in known:
                dangling.add((v, 'input', p))
    w('## 四、反向检查：引用了未注册物品的配方（%d）' % len({d[0] for d in dangling}))
    w('')
    if dangling:
        for o, kind, p in sorted(dangling, key=lambda x: x[0]):
            w('- `%s` (%s) ← %s' % (o, kind, p.relative_to(RES).as_posix()))
    else:
        w('- 无')
    w('')

    text = '\n'.join(lines)
    print(text)
    if md_path:
        Path(md_path).write_text(text, encoding='utf-8')
        print('\n[已写入] %s' % md_path)


def short_any(outputs, sid):
    return any(o.split(':')[-1] == sid for o in outputs)


if __name__ == '__main__':
    main()
