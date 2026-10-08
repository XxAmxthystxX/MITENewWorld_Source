package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.tooltip.AlloyDetailTipComponent;
import com.mitenewworld.screen.tooltip.AlloyTooltipHelper;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.screen.tooltip.ModToolTipComponent;
import com.mitenewworld.item.component.ModTooltipData;

import com.mitenewworld.item.component.*;
import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.tags.ModBlockTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModToolItem extends Item {

    public static final float ATTACK_SCALE = 0.4f;
    public static final float VANILLA_INTERACTION_RANGE = 3.0f;

    public static final Identifier BASE_ATTACK_DAMAGE_MODIFIER_ID =
            Identifier.of("mitenewworld", "base_attack_damage");
    public static final Identifier BASE_ATTACK_SPEED_MODIFIER_ID =
            Identifier.of("mitenewworld", "base_attack_speed");
    public static final Identifier BASE_ATTACK_RANGE_MODIFIER_ID =
            Identifier.of("mitenewworld", "base_attack_range");

    protected final ToolType toolType;

    public ModToolItem(ToolType toolType, Settings settings) {
        super(settings);
        this.toolType = toolType;
    }

    public ToolType toolType() { return toolType; }

    public static Settings toolSettings() {
        return new Settings().maxCount(1);
    }

    // ============================================================
    //  合成
    // ============================================================

    public ItemStack createFromAlloy(List<AlloyComponent> inputs) {
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException("No inputs");
        }
        return createFromAlloy(AlloyComponent.sumForEquipment( inputs, AlloyComponent.averageMetals(inputs)));
    }

    /** 直接用算好的总组件生产（供品质共鸣等后处理） */
    public ItemStack createFromAlloy(AlloyComponent total) {
        ItemStack stack = new ItemStack(this);

        // ① 最大耐久
        int maxDur = Math.max(1, (int) (total.durability() * toolType.durabilityMultiplier()));
        stack.set(DataComponentTypes.MAX_DAMAGE, maxDur);

        // ② 攻击伤害
        float attackDamage = total.hardness()
                * toolType.attackMultiplier()
                * ATTACK_SCALE;

        // ③ 攻速 = 形状基础 + 重量惩罚
        float attackSpeed = toolType.baseAttackSpeed()
                - total.weight() / 1000f;

        // ④ 攻击范围修正
        float rangeBonus = (float) (toolType.attackRange() - VANILLA_INTERACTION_RANGE);

        // ⑤ 属性
        AttributeModifiersComponent.Builder attrs = AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND);

        if (rangeBonus != 0f) {
            attrs.add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(BASE_ATTACK_RANGE_MODIFIER_ID, rangeBonus, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND);
        }

        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, attrs.build());

        // ⑥ 武器组件
        stack.set(DataComponentTypes.WEAPON, new WeaponComponent(toolType.attackWearBase(), toolType.disableBlocking()));

        // ⑦ 数据
        stack.set(ModDataComponentTypes.ALLOY_COMPONENT, total);

        // ⑧ 命名：合金成分 + 物品名（与 ModDefaultAlloys.named 一致）
        stack.set(DataComponentTypes.CUSTOM_NAME, alloyName(total));

        return stack;
    }

    /** 金属成分命名：金属（多金属用 - 连接）+ " · " + 物品基础名，不加斜体 */
    private Text alloyName(AlloyComponent comp) {
        List<String> disp = comp.display();
        if (disp.isEmpty()) {
            MutableText base = Text.translatable(this.getTranslationKey());
            base.setStyle(base.getStyle().withItalic(false));
            return base;
        }
        MutableText name = Text.empty();
        for (int i = 0; i < disp.size(); i++) {
            if (i > 0) {
                name.append(Text.literal("-"));
            }
            name.append(Text.translatable("metal.mitenewworld." + disp.get(i)));
        }
        name.append(Text.literal(" · "));
        name.append(Text.translatable(this.getTranslationKey()));
        name.setStyle(name.getStyle().withItalic(false));
        return name;
    }

    // ============================================================
    //  运行时
    // ============================================================

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return toolType.behavior().useOnBlock(context, toolType);
    }

    @Override
    public boolean isCorrectForDrops(ItemStack stack, net.minecraft.block.BlockState state) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return false;
        }
        TagKey<Block> incorrectTag = MiningTier.of(comp.hardness()).incorrectTag();
        if (state.isIn(incorrectTag)) {
            return false;
        }
        return state.isIn(toolType.mineableTag());
    }

    @Override
    public float getMiningSpeed(ItemStack stack, net.minecraft.block.BlockState state) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return 1.0f;
        }
        if (!state.isIn(toolType.mineableTag())) {
            return 1.0f;
        }
        return (1f + comp.hardness() / 20f) * toolType.miningMultiplier();
    }

    @Override
    public boolean postMine(ItemStack stack, World world,
                            net.minecraft.block.BlockState state,
                            BlockPos pos, LivingEntity miner) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return false;
        }

        float blockHardness = state.getHardness(world, pos);
        float mult = stack.isSuitableFor(state)
                ? toolType.suitableWearMultiplier()
                : toolType.unsuitableWearMultiplier();
        float toughness = comp.toughness();
        int loss = Math.max(1, Math.round(blockHardness * mult / (1f + toughness)));

        if (!world.isClient()) {
            stack.damage(loss, miner, EquipmentSlot.MAINHAND);
        }
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return;
        }

        float toughness = comp.toughness();
        int loss = Math.max(1, Math.round(toolType.attackWearBase() / (1f + toughness)));

        stack.damage(loss, attacker, EquipmentSlot.MAINHAND);
    }
    @Override
    @Environment(EnvType.CLIENT)
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return Optional.empty();
        }

        if (AlloyTooltipHelper.isHoldingMagnifier()) {
            return Optional.of(new ModTooltipData(AlloyDetailTipComponent.of(comp)));
        }
        return Optional.of(new ModTooltipData(ModToolTipComponent.primary(stack, comp)));
    }

    public enum ToolType {

        //              攻击乘数 攻速   挖掘乘数 耐久乘数 攻击损耗 合适磨损 不适合磨损 范围   格挡   tag                            行为
        SWORD(          1.5f,  -2.4f,  0.8f,   1.0f,   15,    1.0f,   2.0f,    2.0,  2.0f, ModBlockTags.SWORD_MINEABLE,   ToolBehavior.Kind.NONE),
        DAGGER(         1.6f,  -1.6f,  0.8f,   0.5f,   15,    1.0f,   2.0f,    0.5,  0.0f, ModBlockTags.SWORD_MINEABLE,   ToolBehavior.Kind.NONE),
        AXE(            0.9f,  -3.1f,  1.0f,   1.2f,   30,    1.0f,   2.0f,    1.0,  3.0f, ModBlockTags.AXE_MINEABLE,     ToolBehavior.Kind.STRIP),
        HATCHET(        1.5f,  -2.6f,  1.0f,   0.8f,   30,    1.0f,   2.0f,    1.0,  0.0f, ModBlockTags.AXE_MINEABLE,     ToolBehavior.Kind.STRIP),
        BATTLEAXE(      0.7f,  -3.4f,  0.8f,   1.5f,   15,    1.2f,   2.5f,    1.0,  5.0f, ModBlockTags.AXE_MINEABLE,     ToolBehavior.Kind.STRIP),
        WARHAMMER(      0.65f, -3.4f,  0.8f,   1.8f,   15,    1.0f,   1.5f,    0.5,  7.0f, ModBlockTags.PICKAXE_MINEABLE, ToolBehavior.Kind.NONE),
        SCYTHE(         1.2f,  -3.0f,  1.2f,   1.0f,   30,    1.0f,   2.0f,    0.5,  0.0f, ModBlockTags.SCYTHE_MINEABLE,  ToolBehavior.Kind.NONE),
        PICKAXE(        0.8f,  -2.8f,  1.2f,   1.2f,   30,    1.0f,   2.0f,    1.0,  3.0f, ModBlockTags.PICKAXE_MINEABLE, ToolBehavior.Kind.NONE),
        HOE(            0.3f,  -3.0f,  1.0f,   1.5f,   45,    1.0f,   2.0f,    0.5,  0.0f, ModBlockTags.SHOVEL_MINEABLE,  ToolBehavior.Kind.TILL),
        SHOVEL(         1.3f,  -3.0f,  1.2f,   1.0f,   45,    1.0f,   2.0f,    1.0,  0.0f, ModBlockTags.SHOVEL_MINEABLE,  ToolBehavior.Kind.FLATTEN),
        MATTOCK(        0.6f,  -3.0f,  0.8f,   1.0f,   30,    1.0f,   2.0f,    1.0,  0.0f, ModBlockTags.SHOVEL_MINEABLE,  ToolBehavior.Kind.TILL),
        SHEARS(         0.4f,  -3.0f,  1.0f,   0.8f,   45,    0.8f,   2.5f,    0.0,  0.0f, ModBlockTags.SHEARS_MINEABLE,  ToolBehavior.Kind.SHEAR),
        // 铸造锤：攻击极低、不能采矿（空 tag → 无正确掉落）、只能锻造
        CASTING_HAMMER( 0.1f,  -3.0f,  0.0f,   1.0f,   15,    1.0f,   2.0f,    1.0,  0.0f, ModBlockTags.CASTING_HAMMER_MINEABLE, ToolBehavior.Kind.NONE);

        private final float attackMultiplier;
        private final float baseAttackSpeed;
        private final float miningMultiplier;
        private final float durabilityMultiplier;
        private final int   attackWearBase;
        private final float suitableWearMultiplier;
        private final float unsuitableWearMultiplier;
        private final double attackRange;
        private final float disableBlocking;
        private final TagKey<Block> mineableTag;
        private final ToolBehavior.Kind behaviorKind;

        ToolType(float attackMultiplier, float baseAttackSpeed,
                 float miningMultiplier, float durabilityMultiplier,
                 int attackWearBase,
                 float suitableWearMultiplier, float unsuitableWearMultiplier,
                 double attackRange, float disableBlocking,
                 TagKey<Block> mineableTag, ToolBehavior.Kind behaviorKind) {
            this.attackMultiplier = attackMultiplier;
            this.baseAttackSpeed = baseAttackSpeed;
            this.miningMultiplier = miningMultiplier;
            this.durabilityMultiplier = durabilityMultiplier;
            this.attackWearBase = attackWearBase;
            this.suitableWearMultiplier = suitableWearMultiplier;
            this.unsuitableWearMultiplier = unsuitableWearMultiplier;
            this.attackRange = attackRange;
            this.disableBlocking = disableBlocking;
            this.mineableTag = mineableTag;
            this.behaviorKind = behaviorKind;
        }

        public float attackMultiplier()          { return attackMultiplier; }
        public float baseAttackSpeed()           { return baseAttackSpeed; }
        public float miningMultiplier()          { return miningMultiplier; }
        public float durabilityMultiplier()      { return durabilityMultiplier; }
        public int   attackWearBase()            { return attackWearBase; }
        public float suitableWearMultiplier()    { return suitableWearMultiplier; }
        public float unsuitableWearMultiplier()  { return unsuitableWearMultiplier; }
        public double attackRange()              { return attackRange; }
        public float disableBlocking()           { return disableBlocking; }
        public TagKey<Block> mineableTag()       { return mineableTag; }
        public ToolBehavior behavior()           { return ToolBehavior.of(behaviorKind); }
    }

    /**
     * 硬度 → 挖掘等级映射。
     *
     * 硬度 5~100 分 5 档，对应原版五级工具的"不能正确掉落" tag。
     * 合金工具构建时按硬度取对应 tag，替代写死的单一 tag。
     */
    public enum MiningTier {
        WOODEN   ( 5f,  20f, BlockTags.INCORRECT_FOR_WOODEN_TOOL),
        STONE    (20f,  40f, BlockTags.INCORRECT_FOR_STONE_TOOL),
        IRON     (40f,  55f, BlockTags.INCORRECT_FOR_IRON_TOOL),
        DIAMOND  (55f,  80f, BlockTags.INCORRECT_FOR_DIAMOND_TOOL),
        NETHERITE(80f, 100f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL);

        private final float min;
        private final float max;
        private final TagKey<Block> incorrectTag;

        MiningTier(float min, float max, TagKey<Block> incorrectTag) {
            this.min = min;
            this.max = max;
            this.incorrectTag = incorrectTag;
        }

        public float min() { return min; }
        public float max() { return max; }
        public TagKey<Block> incorrectTag() { return incorrectTag; }

        public static MiningTier of(float hardness) {
            for (MiningTier t : values()) {
                if (hardness >= t.min && hardness < t.max) {
                    return t;
                }
            }
            // 超过 100 归入 NETHERITE，低于 5 归入 WOODEN
            return hardness >= 100f ? NETHERITE : WOODEN;
        }
    }
}

