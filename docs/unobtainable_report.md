# 物品可获得性检查报告

物品总数（ModItems 注册 + 有物品形式的方块）: **216**
其中方块物品: 75
配方文件: 977，铸造模板: 48，战利品掉落(本 mod): 52
闭包迭代轮数: 200，推导出的本 mod 物品: 201

## 一、完全没有任何产出源的物品（16）

既不出现在任何配方 result，也不是战利品掉落 / 铸造模板产出 / 合金炉产出。

### 类别概览

- 其它：16

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

### 1b. 无配方产出，但 Java 代码有引用（4）—— 多半由代码逻辑产出，需人工确认

- `erosion_leaves` ← com/mitenewworld/registry/ModItemGroups.java:148
- `erosion_log` ← com/mitenewworld/registry/ModItemGroups.java:149
- `underworld_dirt` ← com/mitenewworld/registry/ModItemGroups.java:144
- `underworld_grass` ← com/mitenewworld/registry/ModItemGroups.java:145

## 二、配方闭包内无法推导出的物品（16）

**其它（16）**

  - `banana`
  - `beef_stew`
  - `chicken_soup`
  - `cream_of_mushroom_soup`
  - `debug_item`
  - `dough`
  - `erosion_leaves`
  - `erosion_log`
  - `ice_cream`
  - `mashed_potato`
  - `onion`
  - `orange`
  - `sorbet`
  - `underworld_dirt`
  - `underworld_grass`
  - `vegetable_soup`


## 三、有产出源但原料缺口导致拿不到（0）

这些物品有配方产出，但配方的某些原料本身不可获得（或只能通过不可获得物合成）。

- 无

## 四、反向检查：引用了未注册物品的配方（0）

- 无
