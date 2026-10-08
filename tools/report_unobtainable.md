# 物品可获得性检查报告

物品总数（ModItems 注册 + 有物品形式的方块）: **216**
其中方块物品: 75
配方文件: 977，铸造模板: 48，战利品掉落(本 mod): 52
闭包迭代轮数: 200，推导出的本 mod 物品: 181

## 一、完全没有任何产出源的物品（36）

既不出现在任何配方 result，也不是战利品掉落 / 铸造模板产出 / 合金炉产出。

### 类别概览

- 其它：17
- 锈蚀铁工具：12
- 桶类（空桶 / 水桶 / 岩浆桶 / 石桶）：7

### 1a. 配方/掉落都没有，**Java 代码里也从未引用**（12）—— 真·孤儿物品

- `banana`
- `beef_stew`
- `chicken_soup`
- `cream_of_mushroom_soup`
- `debug_item`
- `dough`
- `ice_cream`
- `mashed_potato`
- `onion`
- `orange`
- `sorbet`
- `vegetable_soup`

### 1b. 无配方产出，但 Java 代码有引用（24）—— 多半由代码逻辑产出，需人工确认

- `erosion_leaves` ← com/mitenewworld/registry/ModItemGroups.java:148
- `erosion_log` ← com/mitenewworld/registry/ModItemGroups.java:149
- `gold_ore_netherrack` ← com/mitenewworld/datagen/ModENUSLanProvider.java:292, com/mitenewworld/registry/ModItemGroups.java:115
- `rusted_iron_axe` ← com/mitenewworld/datagen/ModENUSLanProvider.java:86, com/mitenewworld/registry/ModItemGroups.java:53
- `rusted_iron_battleaxe` ← com/mitenewworld/datagen/ModENUSLanProvider.java:43, com/mitenewworld/registry/ModItemGroups.java:54
- `rusted_iron_dagger` ← com/mitenewworld/datagen/ModENUSLanProvider.java:35, com/mitenewworld/registry/ModItemGroups.java:50
- `rusted_iron_hatchet` ← com/mitenewworld/datagen/ModENUSLanProvider.java:111, com/mitenewworld/registry/ModItemGroups.java:56
- `rusted_iron_hoe` ← com/mitenewworld/datagen/ModENUSLanProvider.java:94, com/mitenewworld/registry/ModItemGroups.java:55
- `rusted_iron_mattock` ← com/mitenewworld/datagen/ModENUSLanProvider.java:102, com/mitenewworld/registry/ModItemGroups.java:51
- `rusted_iron_pickaxe` ← com/mitenewworld/datagen/ModENUSLanProvider.java:76, com/mitenewworld/registry/ModItemGroups.java:52
- `rusted_iron_scythe` ← com/mitenewworld/datagen/ModENUSLanProvider.java:59, com/mitenewworld/registry/ModItemGroups.java:49
- `rusted_iron_shears` ← com/mitenewworld/datagen/ModENUSLanProvider.java:119, com/mitenewworld/registry/ModItemGroups.java:48
- `rusted_iron_shovel` ← com/mitenewworld/datagen/ModENUSLanProvider.java:65, com/mitenewworld/registry/ModItemGroups.java:47
- `rusted_iron_sword` ← com/mitenewworld/datagen/ModENUSLanProvider.java:27, com/mitenewworld/registry/ModItemGroups.java:46
- `rusted_iron_warhammer` ← com/mitenewworld/datagen/ModENUSLanProvider.java:51, com/mitenewworld/registry/ModItemGroups.java:45
- `stone_adamantium_bucket` ← com/mitenewworld/datagen/ModENUSLanProvider.java:152, com/mitenewworld/registry/ModItemGroups.java:85
- `stone_ancient_metal_bucket` ← com/mitenewworld/datagen/ModENUSLanProvider.java:151, com/mitenewworld/registry/ModItemGroups.java:84
- `stone_copper_bucket` ← com/mitenewworld/datagen/ModENUSLanProvider.java:146, com/mitenewworld/registry/ModItemGroups.java:79
- `stone_gold_bucket` ← com/mitenewworld/datagen/ModENUSLanProvider.java:148, com/mitenewworld/registry/ModItemGroups.java:81
- `stone_iron_bucket` ← com/mitenewworld/datagen/ModENUSLanProvider.java:149, com/mitenewworld/registry/ModItemGroups.java:82
- `stone_mithril_bucket` ← com/mitenewworld/datagen/ModENUSLanProvider.java:150, com/mitenewworld/registry/ModItemGroups.java:83
- `stone_silver_bucket` ← com/mitenewworld/datagen/ModENUSLanProvider.java:147, com/mitenewworld/registry/ModItemGroups.java:80
- `underworld_dirt` ← com/mitenewworld/registry/ModItemGroups.java:144
- `underworld_grass` ← com/mitenewworld/registry/ModItemGroups.java:145

## 二、配方闭包内无法推导出的物品（36）

**其它（17）**

  - `banana`
  - `beef_stew`
  - `chicken_soup`
  - `cream_of_mushroom_soup`
  - `debug_item`
  - `dough`
  - `erosion_leaves`
  - `erosion_log`
  - `gold_ore_netherrack`
  - `ice_cream`
  - `mashed_potato`
  - `onion`
  - `orange`
  - `sorbet`
  - `underworld_dirt`
  - `underworld_grass`
  - `vegetable_soup`

**锈蚀铁工具（12）**

  - `rusted_iron_axe`
  - `rusted_iron_battleaxe`
  - `rusted_iron_dagger`
  - `rusted_iron_hatchet`
  - `rusted_iron_hoe`
  - `rusted_iron_mattock`
  - `rusted_iron_pickaxe`
  - `rusted_iron_scythe`
  - `rusted_iron_shears`
  - `rusted_iron_shovel`
  - `rusted_iron_sword`
  - `rusted_iron_warhammer`

**桶类（空桶 / 水桶 / 岩浆桶 / 石桶）（7）**

  - `stone_adamantium_bucket`
  - `stone_ancient_metal_bucket`
  - `stone_copper_bucket`
  - `stone_gold_bucket`
  - `stone_iron_bucket`
  - `stone_mithril_bucket`
  - `stone_silver_bucket`


## 三、有产出源但原料缺口导致拿不到（0）

这些物品有配方产出，但配方的某些原料本身不可获得（或只能通过不可获得物合成）。

- 无

## 四、反向检查：引用了未注册物品的配方（0）

- 无
