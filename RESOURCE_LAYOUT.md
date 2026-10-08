# resources 目录结构与迁移约束

整理于 2026-09-29。文件数 4275 → 4223（51 个废弃贴图已移出打包范围）。

## 先看这条：哪些目录不能动

Minecraft 资源包/数据包有硬性约定，以下路径**改了就会坏**，重排时请绕开：

| 路径 | 为什么不能动 |
|---|---|
| `assets/mitenewworld/items/` | 文件名必须等于物品 ID，天然不能分子目录 |
| `assets/mitenewworld/blockstates/` | 文件名必须等于方块 ID，同上 |
| `data/minecraft/loot_table/entities/` | 覆盖原版实体战利品表，路径必须与原版逐字一致 |
| `data/mitenewworld/loot_table/blocks/` | 方块按 `blocks/<方块ID>` 隐式查找战利品表，改路径会导致方块不掉东西 |
| `textures/entity/equipment/{humanoid,humanoid_leggings,horse_body}/` | `equipment/*.json` 里的 `"texture"` 是**相对该图层目录**解析的。填 `mitenewworld:copper` 实际指向 `textures/entity/equipment/humanoid/copper.png` |

**可以自由重排的：** `textures/**`、`models/**`、`data/mitenewworld/recipe/**`。
注意 `data/**/recipe/` 加子目录会改变配方 ID（本项目无代码按 ID 引用配方，安全）。

> 贴图路径与模型路径在 JSON 引用里写法完全相同（都形如 `mitenewworld:item/xxx`），
> 因此搬动时**贴图与同名模型要一起搬**，一次替换才能同时覆盖两种引用。

## 贴图 textures/

```
textures/
  block/      方块：brick 砖块 / machine 机械多方块 / metal 金属块
              ore 矿石(+overlay 矿点层) / plant 植物 / terrain 地形 / misc 兜底
  item/       物品：alloy 合金 / armor 护甲 / buckets 桶 / foods 食物
              ingredients 材料 / tools 工具
  entity/     实体：equipment 装备图层(勿动) / mob 生物
  gui/        一切画在屏幕上的东西
    container/    界面面板底图（高炉、熔炉、铸造台、工作台）
    widget/       按钮、进度条、槽位、图标、状态提示
    recipe_book/  配方书专用槽位状态
    hud/          HUD 叠加层
```

## 配方 data/mitenewworld/recipe/

按产出物类型分目录（共 898 个）：

| 目录 | 数量 | 内容 |
|---|---|---|
| building | 476 | 建筑方块 |
| materials | 188 | 锭、粒、染料、碎片等材料 |
| smelting | 68 | 熔炼配方（`mitenewworld:modfurnace`） |
| food | 58 | 食物 |
| transport | 46 | 船、筏、矿车、轨、鞍、挽具 |
| redstone | 21 | 红石元件 |
| smithing | 21 | 锻造模板与纹饰 |
| tools | 11 | 工具武器 |
| armor | 7 | 护甲 |
| misc | 2 | 兜底（烟花、唱片） |

## 归档

`_archive/unused_textures/`（项目根，不在 `src/` 下，**不会打进 jar**）：
原 `textures/unuse/` 的 50 个废弃贴图 + 1 个未被引用的 `temp_casting_table.png`。

## 自检脚本

```bash
# 校验是否有悬空引用（assets 的 mitenewworld: 引用 + Java 的 textures/xxx.png）
python tools/validate_refs.py src/main/resources

# 统计哪些贴图/模型没被引用（判断死文件时用）
python tools/analyze_refs.py src/main/resources
```

`validate_refs.py` 已排除两类已知假阳性：代码注册的染色源 id（`alloy`、`casting_table`）
和 `Identifier.ofVanilla`（指向原版贴图）。
