# -*- coding: utf-8 -*-
"""
生成铸造台（铁砧造型）的模型 + 染色底图。

来源
----
几何：直接沿用 assets/temp/models/block/base_*_anvil.json 里的铁砧造型
      （底座 / 细腰 / 腰柱 / 砧面 四个 element，与原版 AnvilBlock 的 VoxelShape 一致）。
纹理：以 assets/temp/models 里的**银砧**那套做底模
      （tbase_silver_anvil.png + silver_top_damaged_0/1/2.png）。
      temp 只有 0/1/2 三档损伤，铸造台有 4 档，damaged_3 在 damaged_2 基础上续裂纹。

调色：合金块那种（AlloyTintSource / ColorProviderRegistry.BLOCK）
--------------------------------------------------------------
Minecraft 的 tint 是 **乘法混合**，所以走 tint 的底图必须是"接近白的明暗图"——
参考 textures/block/metal/alloy_block.png：平均 246，整体压在 230~255。
银砧原图平均 193（带一点暖偏色），直接拿去乘会让每种金属都偏暗 24%，
所以这里先把银图**去色成灰度**，再线性拉伸到近白区间 [228, 255]（相对对比完全保留，
只是整体提亮），颜色全部交给 tint。

  body：独立归一化（自身 179..206 -> 228..255）
  top ：三档共用一套映射（165..219 -> 228..255），damage 越深裂纹越多越暗，
        归一化后自然呈现"越破对比越强"的递进
  damaged_3：damaged_2 的裂纹压深 + 沿既有裂纹再长出一截

UV 布局（temp 铁砧模型取样区，16x16）
------------------------------------
  body：全图 16x16 都取样（各侧面/顶底分区重叠，按整块金属质感画即可）
  top ：只有 x3..12 这一条被 up 面取样（uv [3,0,13,16]），两侧透明边不参与
"""
import json
import os
import random
from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "mitenewworld")
TEMP = os.path.join(ROOT, "src", "main", "resources", "assets", "temp", "models")
OUT = os.path.join(ASSETS, "textures", "block", "machine", "castingtable")
MODEL_DIR = os.path.join(ASSETS, "models", "block", "machine", "castingtable")

# ---- 近白目标区间（与 alloy_block.png 的 230~255 / 均值 246 对齐）----
LO = 228
HI = 255

# 判定"已有磨损"的阈值（银砧原图：底噪 ~190，裂纹 ~165..185）
WEAR_THRESHOLD = 186
# damaged_3 相对 damaged_2 额外长出的裂纹像素数
EXTRA_WEAR = 18

SIZE = 16
TEX = "mitenewworld:block/machine/castingtable/casting_table_"

# 全部 13 种金属（与 ModAlloyMetals 的常量顺序一致）
METALS = ["copper", "silver", "iron", "titanium", "mithril", "adamantium",
          "ancient_metal", "tin", "gold", "aluminium", "platinum", "iridium",
          "starlight"]

FACING_Y = {"north": 0, "east": 90, "south": 180, "west": 270}


# ============================================================
#  纹理：银砧底模 -> 去色 -> 拉到近白
# ============================================================

def load_gray(name):
    """读 temp 的银砧图，返回 (list[list[int]] 灰度, list[list[int]] alpha)"""
    im = Image.open(os.path.join(TEMP, name)).convert("RGBA")
    if im.size != (SIZE, SIZE):
        raise ValueError("%s 尺寸不是 16x16：%s" % (name, im.size))
    px = im.load()
    gray = [[0] * SIZE for _ in range(SIZE)]
    alpha = [[0] * SIZE for _ in range(SIZE)]
    for y in range(SIZE):
        for x in range(SIZE):
            r, g, b, a = px[x, y]
            # 去色：银图本身带 r>g=b 的暖偏色，颜色交给 tint，这里只留明暗
            gray[y][x] = round(0.299 * r + 0.587 * g + 0.114 * b)
            alpha[y][x] = a
    return gray, alpha


def opaque_values(grays):
    vals = []
    for gray, alpha in grays:
        for y in range(SIZE):
            for x in range(SIZE):
                if alpha[y][x] > 0:
                    vals.append(gray[y][x])
    return vals


def stretch(gray, alpha, lo_src, hi_src):
    """把 [lo_src, hi_src] 线性映射到近白区间 [LO, HI]"""
    span = max(1, hi_src - lo_src)
    out = [[0] * SIZE for _ in range(SIZE)]
    for y in range(SIZE):
        for x in range(SIZE):
            if alpha[y][x] == 0:
                out[y][x] = 0
                continue
            t = (gray[y][x] - lo_src) / span
            t = 0.0 if t < 0.0 else (1.0 if t > 1.0 else t)
            out[y][x] = round(LO + (HI - LO) * t)
    return out


def extend_wear(gray, alpha, seed=20260930, extra=EXTRA_WEAR):
    """damaged_3：既有裂纹压深，再沿裂纹末端长出 extra 个新裂纹像素"""
    wear = [(x, y) for y in range(SIZE) for x in range(SIZE)
            if alpha[y][x] > 0 and gray[y][x] < WEAR_THRESHOLD]
    for x, y in wear:
        gray[y][x] = max(0, gray[y][x] - 6)

    rng = random.Random(seed)
    seen = set(wear)
    cand = []
    for x, y in wear:
        for dx, dy in ((-1, 0), (1, 0), (0, -1), (0, 1)):
            nx, ny = x + dx, y + dy
            if 0 <= nx < SIZE and 0 <= ny < SIZE and alpha[ny][nx] > 0 and (nx, ny) not in seen:
                cand.append((nx, ny))
                seen.add((nx, ny))
    rng.shuffle(cand)
    for x, y in cand[:extra]:
        # 新裂纹取"比周围更暗"的值，保证归一化后仍是明显的暗线
        gray[y][x] = min(gray[y][x], WEAR_THRESHOLD - 8)
    return gray


def to_image(gray, alpha):
    im = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    px = im.load()
    for y in range(SIZE):
        for x in range(SIZE):
            v = gray[y][x]
            px[x, y] = (v, v, v, alpha[y][x])
    return im


def build_textures():
    body_g, body_a = load_gray("tbase_silver_anvil.png")
    tops = [load_gray("silver_top_damaged_%d.png" % d) for d in range(3)]
    # damaged_3 由 damaged_2 续出来（temp 只到 2）
    d3_g = [row[:] for row in tops[2][0]]
    tops.append((extend_wear(d3_g, tops[2][1]), tops[2][1]))

    body_vals = opaque_values([(body_g, body_a)])
    body_lo, body_hi = min(body_vals), max(body_vals)
    top_vals = opaque_values(tops)
    top_lo, top_hi = min(top_vals), max(top_vals)

    files = {"casting_table_body.png": to_image(stretch(body_g, body_a, body_lo, body_hi), body_a)}
    for d, (g, a) in enumerate(tops):
        files["casting_table_top_damaged_%d.png" % d] = to_image(stretch(g, a, top_lo, top_hi), a)

    print("底模：tbase_silver_anvil %d..%d -> %d..%d" % (body_lo, body_hi, LO, HI))
    print("砧面：silver_top_damaged_0..3 共用 %d..%d -> %d..%d" % (top_lo, top_hi, LO, HI))
    return files


# ============================================================
#  模型：铁砧几何（沿用 assets/temp 的 base_*_anvil）
# ============================================================

def face(uv, texture, cull=None, rotation=None, tintindex=0):
    f = {"uv": uv, "texture": texture, "tintindex": tintindex}
    if cull:
        f["cullface"] = cull
    if rotation is not None:
        f["rotation"] = rotation
    return f


def anvil_elements():
    """与 temp/base_iron_anvil.json 完全一致，只是每个面补上 tintindex"""
    return [
        {
            "__comment": "Anvil base",
            "from": [2, 0, 2],
            "to": [14, 4, 14],
            "faces": {
                "down":  face([2, 2, 14, 14], "#body", "down", 180),
                "up":    face([2, 2, 14, 14], "#body", None, 180),
                "north": face([2, 12, 14, 16], "#body"),
                "south": face([2, 12, 14, 16], "#body"),
                "west":  face([0, 2, 4, 14], "#body", None, 90),
                "east":  face([4, 2, 0, 14], "#body", None, 270),
            },
        },
        {
            "__comment": "Lower narrow portion",
            "from": [4, 4, 3],
            "to": [12, 5, 13],
            "faces": {
                "up":    face([4, 3, 12, 13], "#body", None, 180),
                "north": face([4, 11, 12, 12], "#body"),
                "south": face([4, 11, 12, 12], "#body"),
                "west":  face([4, 3, 5, 13], "#body", None, 90),
                "east":  face([5, 3, 4, 13], "#body", None, 270),
            },
        },
        {
            "__comment": "Wider section beneath top portion",
            "from": [6, 5, 4],
            "to": [10, 10, 12],
            "faces": {
                "north": face([6, 6, 10, 11], "#body"),
                "south": face([6, 6, 10, 11], "#body"),
                "west":  face([5, 4, 10, 12], "#body", None, 90),
                "east":  face([10, 4, 5, 12], "#body", None, 270),
            },
        },
        {
            "__comment": "Anvil top",
            "from": [3, 10, 0],
            "to": [13, 16, 16],
            "faces": {
                "down":  face([3, 0, 13, 16], "#body", None, 180),
                "up":    face([3, 0, 13, 16], "#top", None, 180),
                "north": face([3, 0, 13, 6], "#body"),
                "south": face([3, 0, 13, 6], "#body"),
                "west":  face([10, 0, 16, 16], "#body", None, 90),
                "east":  face([16, 0, 10, 16], "#body", None, 270),
            },
        },
    ]


def base_model():
    return {
        "parent": "minecraft:block/block",
        "textures": {
            "particle": TEX + "body",
            "body": TEX + "body",
            # 默认给完好档，damaged_* 会覆盖
            "top": TEX + "top_damaged_0",
        },
        "display": {
            "fixed": {
                "rotation": [0, 90, 0],
                "translation": [0, 0, 0],
                "scale": [0.5, 0.5, 0.5],
            }
        },
        "elements": anvil_elements(),
    }


def damaged_model(damage):
    """与原版铁砧一致：只换砧面纹理，几何不变"""
    return {
        "parent": "mitenewworld:block/machine/castingtable/casting_table_base",
        "textures": {"top": TEX + "top_damaged_%d" % damage},
    }


def write_one_json(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(obj, f, indent=2, ensure_ascii=False)
        f.write("\n")


def blockstate():
    """facing(4) x damage(4) = 16 个 variant"""
    variants = {}
    for d in range(4):
        for facing, y in FACING_Y.items():
            variants["damage=%d,facing=%s" % (d, facing)] = {
                "model": "mitenewworld:block/machine/castingtable/casting_table_damaged_%d" % d,
                "y": y,
            }
    return {"variants": variants}


def item_model():
    return {
        "model": {
            "type": "minecraft:model",
            "model": "mitenewworld:block/machine/castingtable/casting_table_damaged_0",
            "tints": [{"type": "mitenewworld:casting_table"}],
        }
    }


def main():
    os.makedirs(OUT, exist_ok=True)

    for name, im in sorted(build_textures().items()):
        im.save(os.path.join(OUT, name))
        vals = [p[0] for p in list(im.getdata()) if p[3] > 0]
        print("%-36s avg=%3d range=%d..%d" % (name, sum(vals) // len(vals), min(vals), max(vals)))

    write_one_json(os.path.join(MODEL_DIR, "casting_table_base.json"), base_model())
    for d in range(4):
        write_one_json(os.path.join(MODEL_DIR, "casting_table_damaged_%d.json" % d), damaged_model(d))
    print("models: casting_table_base + damaged_0..3")

    bs = blockstate()
    im = item_model()
    write_one_json(os.path.join(ASSETS, "blockstates", "mod_casting_table.json"), bs)
    write_one_json(os.path.join(ASSETS, "items", "mod_casting_table.json"), im)
    for metal in METALS:
        block_id = "mod_%s_casting_table" % metal
        write_one_json(os.path.join(ASSETS, "blockstates", block_id + ".json"), bs)
        write_one_json(os.path.join(ASSETS, "items", block_id + ".json"), im)
    print("blockstates + items: 1 通用 + 13 金属")


if __name__ == "__main__":
    main()
