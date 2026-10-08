# -*- coding: utf-8 -*-
"""从历史构建产物(build/libs/mitenewworld.jar)中恢复丢失的配方。

背景：合金系统改造后 mitenewworld:copper_ingot / iron_ingot / ... 等分金属物品
被 alloy_ingot(+组件) 取代，所有引用它们的旧配方变成悬空引用而被清理，
连带把 8 个工作台配方、36 个原版物品配方一起删掉了。

本脚本按映射规则把失效 ID 替换为当前有效 ID 后写回源码配方目录。
"""
import io
import json
import os
import sys
import zipfile

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
JAR = os.path.join(ROOT, 'build', 'libs', 'mitenewworld.jar')
RECIPE_DIR = os.path.join(ROOT, 'src', 'main', 'resources', 'data', 'mitenewworld', 'recipe')

# 失效 ID -> 当前有效 ID
REMAP = {
    'mitenewworld:copper_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:silver_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:gold_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:tin_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:aluminium_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:iron_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:titanium_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:mithril_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:platinum_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:iridium_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:starlight_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:ancient_metal_ingot': 'mitenewworld:alloy_ingot',
    'mitenewworld:adamantium_ingot': 'mitenewworld:alloy_ingot',

    'mitenewworld:copper_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:silver_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:gold_nugget': 'minecraft:gold_nugget',
    'mitenewworld:tin_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:aluminium_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:iron_nugget': 'minecraft:iron_nugget',
    'mitenewworld:titanium_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:mithril_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:platinum_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:iridium_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:starlight_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:ancient_metal_nugget': 'mitenewworld:alloy_nugget',
    'mitenewworld:adamantium_nugget': 'mitenewworld:alloy_nugget',

    'mitenewworld:copper_chain': 'mitenewworld:alloy_chain',
    'mitenewworld:silver_chain': 'mitenewworld:alloy_chain',
    'mitenewworld:gold_chain': 'mitenewworld:alloy_chain',
    'mitenewworld:iron_chain': 'mitenewworld:alloy_chain',
    'mitenewworld:titanium_chain': 'mitenewworld:alloy_chain',
    'mitenewworld:mithril_chain': 'mitenewworld:alloy_chain',
    'mitenewworld:ancient_metal_chain': 'mitenewworld:alloy_chain',
    'mitenewworld:adamantium_chain': 'mitenewworld:alloy_chain',

    'mitenewworld:copper_block': 'mitenewworld:alloy_block',
    'mitenewworld:silver_block': 'mitenewworld:alloy_block',
    'mitenewworld:gold_block': 'mitenewworld:alloy_block',
    'mitenewworld:iron_block': 'mitenewworld:alloy_block',
    'mitenewworld:titanium_block': 'mitenewworld:alloy_block',
    'mitenewworld:mithril_block': 'mitenewworld:alloy_block',
    'mitenewworld:ancient_metal_block': 'mitenewworld:alloy_block',
    'mitenewworld:adamantium_block': 'mitenewworld:alloy_block',
}

# 需要恢复的配方 -> 目标子目录
RESTORE = {
    # 木质悬挂告示牌
    'acacia_hanging_sign': 'building', 'bamboo_hanging_sign': 'building',
    'birch_hanging_sign': 'building', 'cherry_hanging_sign': 'building',
    'crimson_hanging_sign': 'building', 'dark_oak_hanging_sign': 'building',
    'jungle_hanging_sign': 'building', 'mangrove_hanging_sign': 'building',
    'oak_hanging_sign': 'building', 'spruce_hanging_sign': 'building',
    'warped_hanging_sign': 'building',
    # 金属/红石类
    'brush': 'tools', 'clock': 'tools', 'crossbow': 'tools',
    'flint_and_steel': 'tools', 'shield': 'armor', 'spyglass': 'tools',
    'cauldron': 'building', 'iron_bars': 'building', 'iron_door': 'building',
    'iron_trapdoor': 'building', 'lantern': 'building', 'lightning_rod': 'building',
    'detector_rail': 'redstone', 'heavy_weighted_pressure_plate': 'redstone',
    'light_weighted_pressure_plate': 'redstone', 'hopper': 'redstone',
    'piston': 'redstone', 'powered_rail': 'redstone', 'rail': 'redstone',
    'tripwire_hook': 'redstone', 'minecart': 'transport',
    'smithing_table': 'smithing',
    # 粒
    'gold_nugget': 'materials', 'iron_nugget': 'materials',
    'gold_nugget_from_smelting': 'smelting', 'iron_nugget_from_smelting': 'smelting',
    # 食物
    'golden_apple': 'food',
}


def fix_id(value):
    if isinstance(value, str):
        return REMAP.get(value, value)
    if isinstance(value, list):
        out = []
        for v in value:
            nv = REMAP.get(v, v)
            if nv not in out:
                out.append(nv)
        return out
    return value


def main():
    zf = zipfile.ZipFile(JAR)
    written = []
    skipped = []
    unresolved = []

    for name, sub in sorted(RESTORE.items()):
        src = 'data/mitenewworld/recipe/%s.json' % name
        dst = os.path.join(RECIPE_DIR, sub, name + '.json')
        if os.path.exists(dst):
            skipped.append(name)
            continue
        data = json.loads(zf.read(src).decode('utf-8'))
        # 替换材料
        for k, v in list((data.get('key') or {}).items()):
            data['key'][k] = fix_id(v)
        for i, row in enumerate(data.get('input') or []):
            data['input'][i] = fix_id(row)
        # 替换产物
        res = data.get('result')
        if isinstance(res, dict) and isinstance(res.get('id'), str):
            res['id'] = REMAP.get(res['id'], res['id'])
        # 校验：结果里不应再有被删掉的旧金属物品 ID
        blob = json.dumps(data, ensure_ascii=False)
        for old in REMAP:
            if '"%s"' % old in blob:
                unresolved.append((name, old))
        os.makedirs(os.path.dirname(dst), exist_ok=True)
        with io.open(dst, 'w', encoding='utf-8', newline='\n') as f:
            f.write(json.dumps(data, ensure_ascii=False, indent=2) + '\n')
        written.append('%s/%s.json' % (sub, name))

    print('已恢复 %d 个配方：' % len(written))
    for w in written:
        print('  +', w)
    if skipped:
        print('已存在跳过：', skipped)
    if unresolved:
        print('!! 仍有失效 ID：', unresolved)
    return 0 if not unresolved else 1


if __name__ == '__main__':
    sys.exit(main())
