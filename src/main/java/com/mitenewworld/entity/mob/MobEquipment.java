package com.mitenewworld.entity.mob;

import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.ModToolItem;
import com.mitenewworld.registry.ModEntities;
import com.mitenewworld.registry.ModItems;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 怪物自然生成时携带本模组的工具 / 武器。
 *
 * <p>规则：
 * <ul>
 *   <li>有手的敌对怪按当地难度概率携带一件工具或武器。</li>
 *   <li>低阶怪有较高概率携带锈铁工具（固定属性、已损耗）；高阶怪只携带合金工具。</li>
 *   <li>合金工具的金属配比随机生成：1~3 种金属，权重随机后归一化。</li>
 *   <li>骷髅系保留弓弩（副手挂一件近战武器作为掉落来源），
 *       另有 {@link #MELEE_SKELETON_CHANCE} 概率转为不拿弓的近战骷髅——
 *       原版骷髅本身就有近战 AI，不拿弓时会自动切近战。</li>
 * </ul>
 */
public final class MobEquipment {

    private MobEquipment() {
    }

    // ============================================================
    //  配置
    // ============================================================

    /** 只给"世界自然产生"的怪配装，排除指令 / 刷怪蛋 / 桶 / 繁殖等玩家直接介入的来源 */
    private static final Set<SpawnReason> ALLOWED_REASONS = EnumSet.of(
            SpawnReason.NATURAL, SpawnReason.CHUNK_GENERATION, SpawnReason.STRUCTURE,
            SpawnReason.SPAWNER, SpawnReason.PATROL, SpawnReason.JOCKEY, SpawnReason.REINFORCEMENT);

    /** 有手、能持械的敌对怪（含本模组怪物） */
    private static final Set<EntityType<?>> ARMED_MOBS = Set.of(
            EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.HUSK, EntityType.DROWNED,
            EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED, EntityType.WITHER_SKELETON,
            EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
            EntityType.VINDICATOR, EntityType.PILLAGER, EntityType.EVOKER, EntityType.ILLUSIONER, EntityType.VEX,
            ModEntities.SHADOW_ENTITY, ModEntities.SKELETON_KNIGHT_ENTITY);

    /**
     * 骷髅系：天生带弓弩，其中一部分转为近战。
     * 骷髅骑士不在此列——它有自己的弓 / 近战主手切换逻辑，
     * 给它换主手会在它切远程时被覆盖掉，所以它固定走"保留主手、装备挂副手"。
     */
    private static final Set<EntityType<?>> SKELETONS = Set.of(EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED);

    /** 中阶：金属池加入钛 / 铂 */
    private static final Set<EntityType<?>> MID_TIER = Set.of(
            EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,
            EntityType.VINDICATOR, EntityType.PILLAGER, EntityType.VEX, EntityType.ILLUSIONER);

    /** 高阶：秘银 / 艾德曼 / 远古金属 */
    private static final Set<EntityType<?>> HIGH_TIER = Set.of(EntityType.WITHER_SKELETON, EntityType.EVOKER, ModEntities.SHADOW_ENTITY, ModEntities.SKELETON_KNIGHT_ENTITY);

    private static final List<String> LOW_METALS = List.of("copper", "tin", "aluminium", "iron", "silver", "gold");
    private static final List<String> MID_METALS = List.of("copper", "iron", "silver", "gold", "titanium", "platinum");
    private static final List<String> HIGH_METALS = List.of("iron", "titanium", "platinum", "mithril", "adamantium", "ancient_metal");

    /** 合金近战武器 */
    private static final List<Item> ALLOY_WEAPONS = List.of(
            ModItems.ALLOY_SWORD, ModItems.ALLOY_DAGGER, ModItems.ALLOY_BATTLEAXE,
            ModItems.ALLOY_WARHAMMER, ModItems.ALLOY_SCYTHE, ModItems.ALLOY_AXE,
            ModItems.ALLOY_HATCHET, ModItems.ALLOY_MATTOCK);

    /** 合金生产工具 */
    private static final List<Item> ALLOY_TOOLS = List.of(ModItems.ALLOY_PICKAXE, ModItems.ALLOY_SHOVEL, ModItems.ALLOY_HOE, ModItems.ALLOY_SHEARS);

    /** 锈铁近战武器 */
    private static final List<Item> RUSTED_WEAPONS = List.of(
            ModItems.RUSTED_IRON_SWORD, ModItems.RUSTED_IRON_DAGGER, ModItems.RUSTED_IRON_BATTLEAXE,
            ModItems.RUSTED_IRON_WARHAMMER, ModItems.RUSTED_IRON_SCYTHE, ModItems.RUSTED_IRON_AXE,
            ModItems.RUSTED_IRON_HATCHET, ModItems.RUSTED_IRON_MATTOCK);

    /** 锈铁生产工具 */
    private static final List<Item> RUSTED_TOOLS = List.of(ModItems.RUSTED_IRON_PICKAXE, ModItems.RUSTED_IRON_SHOVEL, ModItems.RUSTED_IRON_HOE, ModItems.RUSTED_IRON_SHEARS);

    /** 骷髅系转为近战（不拿弓）的概率，即"近战小白" */
    private static final float MELEE_SKELETON_CHANCE = 0.4f;

    private static final float RUSTED_CHANCE_LOW = 0.5f;
    private static final float RUSTED_CHANCE_MID = 0.15f;

    private static final float RUSTED_DROP_CHANCE = 0.35f;
    private static final float ALLOY_DROP_CHANCE = 0.15f;

    /** 拿到武器而非生产工具的概率 */
    private static final float WEAPON_RATIO = 0.7f;

    // ============================================================
    //  入口
    // ============================================================

    public static void equip(MobEntity mob, Random random, LocalDifficulty difficulty, SpawnReason reason) {
        if (!ALLOWED_REASONS.contains(reason)) {
            return;
        }
        EntityType<?> type = mob.getType();
        if (!ARMED_MOBS.contains(type)) {
            return;
        }

        float clamped = Math.min(3.0f, difficulty.getClampedLocalDifficulty());
        if (random.nextFloat() > 0.25f + 0.10f * clamped) {
            return;
        }

        boolean rusted = random.nextFloat() < rustedChance(type);
        ItemStack held = rusted ? rustedItem(random) : alloyItem(random, metalPool(type));

        boolean ranged = isRanged(mob.getMainHandStack());
        boolean meleeSkeleton = ranged && SKELETONS.contains(type) && random.nextFloat() < MELEE_SKELETON_CHANCE;

        float drop = rusted ? RUSTED_DROP_CHANCE : ALLOY_DROP_CHANCE;
        if (ranged && !meleeSkeleton) {
            // 保留弓弩，近战武器挂副手
            mob.equipStack(EquipmentSlot.OFFHAND, held);
            mob.setEquipmentDropChance(EquipmentSlot.OFFHAND, drop);
        } else {
            // 近战骷髅 / 普通近战怪：直接换掉主手
            mob.equipStack(EquipmentSlot.MAINHAND, held);
            mob.setEquipmentDropChance(EquipmentSlot.MAINHAND, drop);
        }
    }

    // ============================================================
    //  随机合金
    // ============================================================

    /**
     * 随机金属配比：1 种 50%、2 种 40%、3 种 10%，第一种为主金属。
     * 返回归一化前的原始权重，交给 {@link AlloyComponent#compute} 处理。
     */
    public static Map<String, Float> randomMix(Random random, List<String> pool) {
        int count = switch (random.nextInt(10)) {
            case 0, 1, 2, 3, 4 -> 1;
            case 5, 6, 7, 8 -> 2;
            default -> 3;
        };
        count = Math.min(count, pool.size());

        List<String> shuffled = new ArrayList<>(pool);
        shuffle(shuffled, random);

        Map<String, Float> raw = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            float weight = 0.2f + random.nextFloat();
            if (i == 0) {
                weight *= 2.5f;
            }
            raw.put(shuffled.get(i), weight);
        }
        return raw;
    }

    /** 用随机配比生成一件合金工具 */
    public static ItemStack alloyTool(Item item, Random random, List<String> pool) {
        if (!(item instanceof ModToolItem tool)) {
            return new ItemStack(item);
        }
        return tool.createFromAlloy(AlloyComponent.compute(randomMix(random, pool), Set.of()));
    }

    // ============================================================
    //  内部
    // ============================================================

    /** Fisher-Yates 洗牌（Minecraft 的 Random 不继承 java.util.Random） */
    private static void shuffle(List<String> list, Random random) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
        }
    }

    private static List<String> metalPool(EntityType<?> type) {
        if (HIGH_TIER.contains(type)) {
            return HIGH_METALS;
        }
        if (MID_TIER.contains(type)) {
            return MID_METALS;
        }
        return LOW_METALS;
    }

    private static float rustedChance(EntityType<?> type) {
        if (HIGH_TIER.contains(type)) {
            return 0.0f;
        }
        if (MID_TIER.contains(type)) {
            return RUSTED_CHANCE_MID;
        }
        return RUSTED_CHANCE_LOW;
    }

    private static ItemStack alloyItem(Random random, List<String> pool) {
        Item item = random.nextFloat() < WEAPON_RATIO
                ? pick(random, ALLOY_WEAPONS)
                : pick(random, ALLOY_TOOLS);
        return alloyTool(item, random, pool);
    }

    private static ItemStack rustedItem(Random random) {
        Item item = random.nextFloat() < WEAPON_RATIO
                ? pick(random, RUSTED_WEAPONS)
                : pick(random, RUSTED_TOOLS);
        ItemStack stack = new ItemStack(item);
        wear(stack, random, 0.3f, 0.8f);
        return stack;
    }

    private static Item pick(Random random, List<Item> items) {
        return items.get(random.nextInt(items.size()));
    }

    /** 按最大耐久的百分比预先损耗 */
    private static void wear(ItemStack stack, Random random, float min, float max) {
        int maxDamage = stack.getMaxDamage();
        if (maxDamage <= 1) {
            return;
        }
        int damage = (int) (maxDamage * (min + (max - min) * random.nextFloat()));
        stack.set(DataComponentTypes.DAMAGE, Math.min(damage, maxDamage - 1));
    }

    private static boolean isRanged(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof RangedWeaponItem || item == Items.TRIDENT;
    }
}
