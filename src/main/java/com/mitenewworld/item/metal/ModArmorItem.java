package com.mitenewworld.item.metal;
import com.mitenewworld.screen.tooltip.AlloyDetailTipComponent;
import com.mitenewworld.screen.tooltip.AlloyTooltipHelper;
import com.mitenewworld.screen.tooltip.ModArmorTipComponent;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.component.ModTooltipData;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.item.component.*;
import com.mitenewworld.item.metal.AlloyComponent;
import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModArmorItem extends Item {

    public static final float ARMOR_VALUE_SCALE = 24f;
    public static final float MAX_ARMOR_PROTECTION = 80f;
    public static final float ARMOR_WEAR_SCALE = 5f;

    public static final Identifier ALLOY_ARMOR_ASSET =
            Identifier.of(MITENewWorld.MOD_ID, "alloy_armor");
    public static final Identifier ALLOY_CHAIN_ASSET =
            Identifier.of(MITENewWorld.MOD_ID, "alloy_chain");

    protected final ArmorType armorType;
    protected final Identifier assetId;

    public ModArmorItem(ArmorType armorType, Settings settings) {
        super(settings);
        this.armorType = armorType;
        this.assetId = ALLOY_ARMOR_ASSET;
    }

    public ModArmorItem(ArmorType armorType, Identifier assetId, Settings settings) {
        super(settings);
        this.armorType = armorType;
        this.assetId = assetId;
    }

    public ArmorType armorType() { return armorType; }

    /** 板甲工厂 */
    public static ModArmorItem plate(ArmorType type, Settings settings) {
        return new ModArmorItem(type, ALLOY_ARMOR_ASSET, settings);
    }

    /** 锁链甲工厂 */
    public static ModArmorItem chain(ArmorType type, Settings settings) {
        return new ModArmorItem(type, ALLOY_CHAIN_ASSET, settings);
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

        // ① 耐久
        stack.set(DataComponentTypes.MAX_DAMAGE, Math.max(1, (int) total.durability()));

        // ② 属性
        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, buildAttributes(total));

        // ③ 可装备
        stack.set(DataComponentTypes.EQUIPPABLE,
                EquippableComponent.builder(armorType.getEquipmentSlot())
                        .model(RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, assetId))
                        .build());

        // ④ 染色
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(total.color()));

        // ⑤ 数据
        stack.set(ModDataComponentTypes.ALLOY_COMPONENT, total);

        // ⑥ 命名：合金成分 + 物品名（与 ModDefaultAlloys.named 一致）
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

    private AttributeModifiersComponent buildAttributes(AlloyComponent total) {
        AttributeModifiersComponent.Builder b = AttributeModifiersComponent.builder();
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(armorType.getEquipmentSlot());
        Identifier id = Identifier.of(MITENewWorld.MOD_ID, "alloy_armor." + armorType.getName());

        // 护甲值
        b.add(EntityAttributes.ARMOR, new EntityAttributeModifier(id, total.hardness() / ARMOR_VALUE_SCALE, EntityAttributeModifier.Operation.ADD_VALUE), slot);

        // 韧性
        b.add(EntityAttributes.ARMOR_TOUGHNESS, new EntityAttributeModifier(id, total.toughness(), EntityAttributeModifier.Operation.ADD_VALUE), slot);

        // 抗击退
        float kbRes = total.toughness() * 0.15f;
        if (kbRes > 0f) {
            b.add(EntityAttributes.KNOCKBACK_RESISTANCE, new EntityAttributeModifier(id, kbRes, EntityAttributeModifier.Operation.ADD_VALUE), slot);
        }

        // 攻速惩罚
        float atkPenalty = -total.weight() / 1000f;
        if (atkPenalty != 0f) {
            b.add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(id, atkPenalty, EntityAttributeModifier.Operation.ADD_VALUE), slot);
        }

        // 移速惩罚
        float movePenalty = -total.weight() / 1000f;
        if (movePenalty != 0f) {
            b.add(EntityAttributes.MOVEMENT_SPEED, new EntityAttributeModifier(id, movePenalty, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), slot);
        }

        return b.build();
    }
    @Override
    @Environment(EnvType.CLIENT)
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return Optional.empty();
        }
        if (!AlloyTooltipHelper.isShiftDown()) {
            return Optional.empty();
        }

        if (AlloyTooltipHelper.isHoldingMagnifier()) {
            return Optional.of(new ModTooltipData(AlloyDetailTipComponent.of(comp)));
        }
        return Optional.of(new ModTooltipData(ModArmorTipComponent.primary(stack, comp)));
    }
    // ============================================================
    //  mixin 入口
    // ============================================================

    @SuppressWarnings("ForMixin")
    public static float handleDamage(LivingEntity wearer,
                                     float damageAmount,
                                     DamageSource source,
                                     float armor,
                                     float armorToughness) {
        if (damageAmount <= 0f) {
            return 0f;
        }

        float toughness = MathHelper.clamp(0.8f * armorToughness / 10f, 0f, 0.8f);
        float protection = MathHelper.clamp(0.8f * armor, 0f, MAX_ARMOR_PROTECTION);

        float effectiveness;
        ItemStack weapon = source.getWeaponStack();
        if (weapon != null && wearer.getEntityWorld() instanceof ServerWorld sw) {
            effectiveness = MathHelper.clamp(EnchantmentHelper.getArmorEffectiveness( sw, weapon, wearer, source, toughness), 0f, 1f);
        } else {
            effectiveness = toughness;
        }

        float result = damageAmount * (1f - effectiveness) - protection;
        result = MathHelper.clamp(result, 1f, damageAmount);

        if (wearer.getEntityWorld() instanceof ServerWorld) {
            damageOneAlloyPiece(wearer, damageAmount);
        }

        return result;
    }

    private static void damageOneAlloyPiece(LivingEntity wearer, float incomingDamage) {
        List<ItemStack> stacks = new ArrayList<>();
        List<EquipmentSlot> slots = new ArrayList<>();

        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.BODY}) {
            ItemStack s = wearer.getEquippedStack(slot);
            if (s.isEmpty()) {
                continue;
            }
            if (s.get(ModDataComponentTypes.ALLOY_COMPONENT) == null) {
                continue;
            }
            stacks.add(s);
            slots.add(slot);
        }

        if (stacks.isEmpty()) {
            return;
        }

        int idx = wearer.getRandom().nextInt(stacks.size());
        ItemStack chosen = stacks.get(idx);
        EquipmentSlot slot = slots.get(idx);

        AlloyComponent comp = chosen.get(ModDataComponentTypes.ALLOY_COMPONENT);
        float toughness = comp != null ? comp.toughness() : 0f;

        float baseLoss = incomingDamage * ARMOR_WEAR_SCALE;
        int loss = Math.max(1, Math.round(baseLoss / (1f + toughness)));

        chosen.damage(loss, wearer, slot);
    }

    // ============================================================
    //  ArmorType
    // ============================================================

    public enum ArmorType implements StringIdentifiable {
        HELMET(EquipmentSlot.HEAD, "helmet"),
        CHESTPLATE(EquipmentSlot.CHEST, "chestplate"),
        LEGGINGS(EquipmentSlot.LEGS, "leggings"),
        BOOTS(EquipmentSlot.FEET, "boots"),
        BODY(EquipmentSlot.BODY, "body");

        public static final Codec<EquipmentType> CODEC;

        private final EquipmentSlot equipmentSlot;
        private final String name;

        ArmorType(EquipmentSlot equipmentSlot, String name) {
            this.equipmentSlot = equipmentSlot;
            this.name = name;
        }

        public EquipmentSlot getEquipmentSlot() { return equipmentSlot; }
        public String getName() { return name; }

        @Override
        public String asString() { return name; }

        static {
            CODEC = StringIdentifiable.createBasicCodec(EquipmentType::values);
        }
    }
}
