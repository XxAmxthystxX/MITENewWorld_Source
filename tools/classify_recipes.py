"""按产出物把配方分入类型目录。先演练看分布，再加 --apply 落盘。"""
import json
import shutil
import sys
from collections import Counter
from pathlib import Path

EXACT = {
    'bow': 'tools', 'bowl': 'materials', 'book': 'materials', 'clay': 'materials',
    'map': 'materials', 'melon': 'building', 'tnt': 'redstone', 'armor_stand': 'building',
    'end_rod': 'building', 'decorated_pot': 'building', 'creaking_heart': 'building',
    'dried_ghast': 'materials', 'ender_eye': 'materials', 'fire_charge': 'materials',
    'wind_charge': 'materials', 'bundle': 'materials', 'resin_clump': 'materials',
    'wolf_armor': 'armor', 'music_disc_5': 'misc', 'firework_rocket': 'misc',
    'packed_mud': 'building', 'muddy_mangrove_roots': 'building',
}

RULES = [
    ('armor', ['helmet', 'chestplate', 'leggings', 'boots', '_body', 'elytra', 'horse_armor', 'turtle_helmet']),
    ('tools', ['sword', '_axe', 'pickaxe', 'shovel', 'hoe', 'hammer', 'dagger', 'shears', 'scythe',
               'hatchet', 'mattock', 'club', 'cudgel', 'knife', '_bow', 'crossbow', 'arrow', 'shield',
               'trident', 'mace', 'fishing_rod', 'flint_and_steel', 'brush', 'spyglass', 'compass',
               'clock', 'culb', 'cudgel']),
    ('smithing', ['smithing_template', '_trim', 'netherite']),
    ('transport', ['boat', 'raft', 'minecart', '_rail', 'saddle', 'lead', 'harness']),
    ('redstone', ['redstone', 'comparator', 'repeater', 'piston', 'observer', 'hopper', 'dispenser',
                  'dropper', 'target', 'daylight_detector', 'lever', 'tripwire', 'note_block',
                  'jukebox', 'bell', 'lectern', 'lightning_rod', 'sculk_sensor']),
    ('food', ['bread', 'cookie', 'cake', 'pie', 'stew', 'soup', '_apple', 'golden_apple', 'beef',
              'chicken', 'porkchop', 'mutton', 'rabbit', '_fish', 'salmon', 'cod', 'tropical_fish',
              'pufferfish', 'carrot', 'potato', 'baked', 'melon_slice', 'pumpkin_pie', 'mushroom_stew',
              'beetroot', 'dried_kelp', 'sweet_berries', 'glow_berries', 'honey_bottle', 'milk_bucket',
              'egg', 'sugar', 'cocoa_beans', 'cheese', 'salad', 'porridge', 'cereal', 'chocolate',
              'orange', 'banana', 'onion', 'ice_cream', 'sorbet', 'dough', 'mashed_potato',
              'horse_meat', 'bowl_of_milk', 'suspicious_stew', 'rotten_flesh', 'spider_eye']),
    ('materials', ['ingot', 'nugget', '_gem', 'dust', 'powder', 'fragment', '_dye', 'coal', 'charcoal',
                   'lapis', 'emerald', 'diamond', 'quartz', 'stick', 'string', 'leather', 'feather',
                   '_bone', 'bone_meal', 'slime', 'paper', '_book', 'brick', 'clay_ball', 'flint',
                   'scrap', 'wheat', 'seed', 'iron_', 'gold_', 'copper_', 'raw_', 'refining',
                   'blaze_rod', 'ender_pearl', 'magma_cream', 'ghast_tear', 'nether_wart', 'gunpowder',
                   'sugar_cane', 'bamboo', 'wool', 'glass_bottle', 'bucket', 'bolt', 'sinew']),
    ('building', ['planks', '_log', '_wood', 'leaves', 'stairs', '_slab', '_wall', 'fence', 'door',
                  'trapdoor', 'button', 'pressure_plate', 'sign', '_block', 'bricks', 'stone',
                  'cobble', 'andesite', 'granite', 'diorite', 'deepslate', 'tuff', '_ore', 'torch',
                  'ladder', 'chest', 'barrel', 'glass', 'pane', 'carpet', 'concrete', 'terracotta',
                  '_bed', 'banner', 'candle', 'anvil', 'enchanting', 'brewing', 'cauldron',
                  'grindstone', 'loom', 'smithing_table', 'stonecutter', 'cartography', 'fletching',
                  'blast_furnace', 'smoker', 'beehive', 'composter', 'flower_pot', 'item_frame',
                  'painting', 'scaffolding', 'sand', 'gravel', 'dirt', 'grass_block', 'moss',
                  'ice', 'snow', 'obsidian', 'netherrack', 'end_stone', 'purpur', 'prismarine',
                  'sea_lantern', 'glowstone', 'shulker', 'sponge', 'bookshelf', 'furnace',
                  'crafting_table', '_table', 'lantern', 'campfire', 'respawn_anchor', 'lodestone',
                  'beacon', 'conduit', 'dragon_egg', 'end_crystal', 'flower', 'sapling', 'mushroom',
                  'coral', 'kelp', 'vine', 'hay', 'jack_o', 'skull', '_head', 'potted',
                  'shelf', 'hyphae', 'basalt', 'world_breaker']),
]


def classify(recipe_type, result_id):
    if 'furnace' in recipe_type:
        return 'smelting'
    name = result_id.split(':')[-1]
    if name in EXACT:
        return EXACT[name]
    for cat, kws in RULES:
        for kw in kws:
            if kw in name:
                return cat
    return 'misc'


def main():
    root = Path(sys.argv[1])
    apply = '--apply' in sys.argv
    files = sorted(root.glob('*.json'))
    counter = Counter()
    plan = []
    for f in files:
        try:
            d = json.loads(f.read_text(encoding='utf-8'))
        except Exception:
            counter['(解析失败)'] += 1
            continue
        r = d.get('result')
        rid = r.get('id') if isinstance(r, dict) else r
        cat = classify(d.get('type', ''), rid or '')
        counter[cat] += 1
        plan.append((f, cat, rid))

    print('模式:', '实际写入' if apply else '演练')
    print(f'配方总数: {len(files)}')
    for k, v in counter.most_common():
        print(f'  {v:5d}  {k}')
    print()
    print('--- misc 兜底的产出物（前 25，用于检查规则是否漏）---')
    miscs = [r for f, c, r in plan if c == 'misc']
    for m in sorted(set(miscs)):
        print('  ', m)
    print(f'  misc 去重后共 {len(set(miscs))} 种')

    if apply:
        for f, cat, rid in plan:
            dst_dir = root / cat
            dst_dir.mkdir(exist_ok=True)
            shutil.move(str(f), str(dst_dir / f.name))
        print('\n已完成移动')


if __name__ == '__main__':
    main()
