package com.mitenewworld.recipe.casting;

import com.mitenewworld.MITENewWorld;

import com.mitenewworld.block.castingtable.CastingTableDefaults;
import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.registry.ModItems;
import com.mitenewworld.item.component.CastingTableComponent;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.AlloyItem;
import com.mitenewworld.item.metal.ModAlloyMetals;
import com.mitenewworld.item.metal.ModArmorItem;
import com.mitenewworld.item.metal.ModSynergies;
import com.mitenewworld.item.metal.ModSynergies.SynergyEffect;
import com.mitenewworld.item.metal.ModToolItem;
import com.mitenewworld.recipe.casting.CastingTemplate.ActiveSlot;
import com.mitenewworld.recipe.casting.CastingTemplate.CraftFunction;
import com.mitenewworld.recipe.casting.CastingTemplate.PropertyCalculator;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 内置铸造模版。
 *
 * 每个模版自身持有：显式的槽位表（位置 + 类型）、属性计算方法、物品合成方法。
 * 全部使用 MC 原版常量注册模式（public static final + Registry.register）。
 *
 * 槽位坐标约定：这里存的是"槽位框的绘制坐标"，真实 {@link net.minecraft.screen.slot.Slot}
 * 的 x/y 由 ScreenHandler 统一 +1（见 ModCastingTableScreenHandler.SLOT_DRAW_OFFSET）。
 *
 * 材料 5 + 铸造台 1 + 工具 12 + 铸造锤 1 + 板甲 5 + 锁链甲 5 = 29。
 */
public final class ModCastingTemplates {

    private ModCastingTemplates() {}

    // ============================================================
    //  注册表
    // ============================================================

    public static final RegistryKey<Registry<CastingTemplate>> REGISTRY_KEY =
            RegistryKey.ofRegistry(Identifier.of(MITENewWorld.MOD_ID,"casting_template"));

    public static final Registry<CastingTemplate> REGISTRY =
            FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();

    private static CastingTemplate register(CastingTemplate template) {
        return Registry.register(REGISTRY, Identifier.of(MITENewWorld.MOD_ID,template.id()), template);
    }

    /** 强制类加载，确保常量在物品/方块注册之后完成。 */
    public static void init() {
        // no-op
    }

    // ============================================================
    //  共享小工具（供各模版 lambda 内部调用，不参与模版定义）
    // ============================================================

    private static final Set<String> FORM_IDS = Set.of("nugget", "block", "chain");

    private static AlloyComponent sumInputs(List<AlloyComponent> inputs) {
        return AlloyComponent.sumForEquipment(inputs, AlloyComponent.averageMetals(inputs));
    }

    private static AlloyComponent stripForm(AlloyComponent comp) {
        List<String> synergies = comp.activeSynergies().stream()
                .filter(id -> !FORM_IDS.contains(id))
                .toList();
        return new AlloyComponent(comp.metals(), Set.of(),
                comp.durability(), comp.toughness(), comp.hardness(),
                comp.enchantRarity(), comp.weight(), synergies);
    }

    private static AlloyComponent markForm(AlloyComponent comp, String form) {
        List<String> synergies = new ArrayList<>(comp.activeSynergies());
        if (!synergies.contains(form)) {
            synergies.add(form);
        }
        return new AlloyComponent(comp.metals(), Set.of(form),
                comp.durability(), comp.toughness(), comp.hardness(),
                comp.enchantRarity(), comp.weight(), List.copyOf(synergies));
    }

    private static int totalUnits(List<ActiveSlot> slots) {
        int units = 0;
        for (ActiveSlot slot : slots) {
            units += switch (slot.type()) {
                case BLOCK -> 9;
                case INGOT, CHAIN -> 1;
                case NUGGET, HANDLE, SINEW -> 0;
            };
        }
        return Math.max(1, units);
    }

    public static int levelForHardness(float hardness) {
        return Math.min(6, ModToolItem.MiningTier.of(hardness).ordinal() + 2);
    }

    /** 工具/护甲通用：把品质并入共鸣后再落到物品上便于回读。 */
    private static ItemStack applyQualityAndStamp(ItemStack stack, AlloyComponent props, int qualityScore) {
        if (stack.isEmpty()) {
            return stack;
        }
        stack.set(ModDataComponentTypes.QUALITY_SCORE, qualityScore);
        return stack;
    }

    // ============================================================
    //  材料模版（标准 3x3 / 单列，坐标不动）
    // ============================================================

    public static final CastingTemplate NUGGET_TO_INGOT = register(new CastingTemplate(
            "nugget_to_ingot", ModItems.ALLOY_INGOT, 1,
            // 3x3 九宫格
            List.of(
                    new ActiveSlot(0, 30, 46, SlotType.NUGGET),
                    new ActiveSlot(1, 56, 46, SlotType.NUGGET),
                    new ActiveSlot(2, 82, 46, SlotType.NUGGET),
                    new ActiveSlot(3, 30, 72, SlotType.NUGGET),
                    new ActiveSlot(4, 56, 72, SlotType.NUGGET),
                    new ActiveSlot(5, 82, 72, SlotType.NUGGET),
                    new ActiveSlot(6, 30, 98, SlotType.NUGGET),
                    new ActiveSlot(7, 56, 98, SlotType.NUGGET),
                    new ActiveSlot(8, 82, 98, SlotType.NUGGET)
            ),
            // 属性计算：9 粒求和，剥掉形态共鸣
            inputs -> stripForm(sumInputs(inputs)),
            // 物品合成：输出 1 锭（材料不吃品质）
            (props, inputs, quality) -> {
                ItemStack stack = AlloyItem.createFromComponent(
                        ModItems.ALLOY_INGOT, AlloyItem.Form.INGOT, props);
                stack.setCount(1);
                return stack;
            }
    ));

    public static final CastingTemplate INGOT_TO_NUGGET = register(new CastingTemplate(
            "ingot_to_nugget", ModItems.ALLOY_NUGGET, 9,
            List.of(
                    new ActiveSlot(0, 56, 72, SlotType.INGOT)
            ),
            inputs -> inputs.getFirst().withFormSynergy("nugget"),
            (props, inputs, quality) -> {
                ItemStack stack = AlloyItem.createFromComponent(
                        ModItems.ALLOY_NUGGET, AlloyItem.Form.NUGGET, props);
                stack.setCount(9);
                return stack;
            }
    ));

    public static final CastingTemplate INGOT_TO_BLOCK = register(new CastingTemplate(
            "ingot_to_block", ModItems.ALLOY_BLOCK, 1,
            // 3x3 九宫格
            List.of(
                    new ActiveSlot(0, 30, 46, SlotType.INGOT),
                    new ActiveSlot(1, 56, 46, SlotType.INGOT),
                    new ActiveSlot(2, 82, 46, SlotType.INGOT),
                    new ActiveSlot(3, 30, 72, SlotType.INGOT),
                    new ActiveSlot(4, 56, 72, SlotType.INGOT),
                    new ActiveSlot(5, 82, 72, SlotType.INGOT),
                    new ActiveSlot(6, 30, 98, SlotType.INGOT),
                    new ActiveSlot(7, 56, 98, SlotType.INGOT),
                    new ActiveSlot(8, 82, 98, SlotType.INGOT)
            ),
            inputs -> markForm(sumInputs(inputs), "block"),
            (props, inputs, quality) -> {
                ItemStack stack = AlloyItem.createFromComponent(
                        ModItems.ALLOY_BLOCK, AlloyItem.Form.BLOCK, props);
                stack.setCount(1);
                return stack;
            }
    ));

    public static final CastingTemplate BLOCK_TO_INGOT = register(new CastingTemplate(
            "block_to_ingot", ModItems.ALLOY_INGOT, 9,
            List.of(
                    new ActiveSlot(0, 56, 72, SlotType.BLOCK)
            ),
            inputs -> AlloyComponent.compute(inputs.getFirst().metals(), Set.of()),
            (props, inputs, quality) -> {
                ItemStack stack = AlloyItem.createFromComponent(
                        ModItems.ALLOY_INGOT, AlloyItem.Form.INGOT, props);
                stack.setCount(9);
                return stack;
            }
    ));

    public static final CastingTemplate NUGGET_TO_CHAIN = register(new CastingTemplate(
            "nugget_to_chain", ModItems.ALLOY_CHAIN, 1,
            // 锁链：4 粒环绕成链环（用户给定坐标）
            List.of(
                    new ActiveSlot(0, 50, 49, SlotType.NUGGET),
                    new ActiveSlot(1, 25, 74, SlotType.NUGGET),
                    new ActiveSlot(2, 50, 99, SlotType.NUGGET),
                    new ActiveSlot(3, 75, 75, SlotType.NUGGET)
            ),
            inputs -> markForm(sumInputs(inputs), "chain"),
            (props, inputs, quality) -> {
                ItemStack stack = AlloyItem.createFromComponent(
                        ModItems.ALLOY_CHAIN, AlloyItem.Form.CHAIN, props);
                stack.setCount(1);
                return stack;
            }
    ));

    // ============================================================
    //  铸造台本体（4 块 + 3 锭）
    // ============================================================

    // 上排 3 块 + 中央 1 块 + 下排 3 锭
    private static final List<ActiveSlot> CASTING_TABLE_SLOTS = List.of(
            new ActiveSlot(0, 18, 49, SlotType.BLOCK),
            new ActiveSlot(1, 47, 49, SlotType.BLOCK),
            new ActiveSlot(2, 74, 49, SlotType.BLOCK),
            new ActiveSlot(3, 47, 76, SlotType.BLOCK),
            new ActiveSlot(4, 22, 99, SlotType.INGOT),
            new ActiveSlot(5, 47, 99, SlotType.INGOT),
            new ActiveSlot(6, 72, 99, SlotType.INGOT)
    );

    public static final CastingTemplate CASTING_TABLE = register(new CastingTemplate(
            "casting_table", ModBlocks.castingTableFor(CastingTableDefaults.FALLBACK_METAL).asItem(), 1,
            CASTING_TABLE_SLOTS,
            // 属性：直接把输入求和（等级 / 耐久由主导金属决定）
            inputs -> sumInputs(inputs),
            // 物品合成：产出主导金属对应的那一台铸造台，
            // 等级 / 耐久走 CastingTableDefaults，保证与物品栏里直接放出来的一致。
            (props, inputs, quality) -> {
                String dominant = props.dominant();
                ItemStack stack = new ItemStack(ModBlocks.castingTableFor(dominant));
                ModAlloyMetals.MetalType metal = ModAlloyMetals.byId(dominant);
                CastingTableComponent comp = metal != null
                        ? CastingTableDefaults.componentFor(metal)
                        : CastingTableComponent.fresh(props.metals(), 1, Math.max(1f, props.durability()));
                stack.set(ModDataComponentTypes.CASTING_TABLE_COMPONENT, comp);
                return stack;
            }
    ));

    // ============================================================
    //  工具（每个模版独立写出自己的槽位 / 属性 / 合成）
    // ============================================================

    public static final CastingTemplate SWORD = register(new CastingTemplate(
            "sword", ModItems.ALLOY_SWORD, 1,
            // 竖向剑身 2 锭 + 下方手柄 1 木棍
            List.of(
                    new ActiveSlot(0, 48, 50, SlotType.INGOT),
                    new ActiveSlot(1, 48, 77, SlotType.INGOT),
                    new ActiveSlot(2, 48, 106, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_SWORD).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate DAGGER = register(new CastingTemplate(
            "dagger", ModItems.ALLOY_DAGGER, 1,
            // 短剑：1 锭 + 1 木棍
            List.of(
                    new ActiveSlot(0, 50, 52, SlotType.INGOT),
                    new ActiveSlot(1, 50, 79, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_DAGGER).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate AXE = register(new CastingTemplate(
            "axe", ModItems.ALLOY_AXE, 1,
            List.of(
                    new ActiveSlot(0, 39, 43, SlotType.INGOT),
                    new ActiveSlot(1, 76, 47, SlotType.INGOT),
                    new ActiveSlot(2, 81, 77, SlotType.INGOT),
                    new ActiveSlot(3, 49, 75, SlotType.HANDLE),
                    new ActiveSlot(4, 49, 106, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_AXE).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate HATCHET = register(new CastingTemplate(
            "hatchet", ModItems.ALLOY_HATCHET, 1,
            List.of(
                    new ActiveSlot(0, 71, 62, SlotType.INGOT),
                    new ActiveSlot(1, 39, 65, SlotType.HANDLE),
                    new ActiveSlot(2, 39, 96, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_HATCHET).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate BATTLEAXE = register(new CastingTemplate(
            "battleaxe", ModItems.ALLOY_BATTLEAXE, 1,
            // 双刃斧：左右各 2 锭，手柄居中 3 木棍
            List.of(
                    new ActiveSlot(0, 19, 50, SlotType.INGOT),
                    new ActiveSlot(1, 19, 79, SlotType.INGOT),
                    new ActiveSlot(2, 78, 50, SlotType.INGOT),
                    new ActiveSlot(3, 78, 80, SlotType.INGOT),
                    new ActiveSlot(4, 49, 58, SlotType.HANDLE),
                    new ActiveSlot(5, 49, 86, SlotType.HANDLE),
                    new ActiveSlot(6, 49, 114, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_BATTLEAXE).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate WARHAMMER = register(new CastingTemplate(
            "warhammer", ModItems.ALLOY_WARHAMMER, 1,
            List.of(
                    new ActiveSlot(0, 23, 43, SlotType.INGOT),
                    new ActiveSlot(1, 23, 68, SlotType.INGOT),
                    new ActiveSlot(2, 52, 55, SlotType.INGOT),
                    new ActiveSlot(3, 81, 43, SlotType.INGOT),
                    new ActiveSlot(4, 81, 68, SlotType.INGOT),
                    new ActiveSlot(5, 52, 84, SlotType.HANDLE),
                    new ActiveSlot(6, 52, 112, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_WARHAMMER).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate SCYTHE = register(new CastingTemplate(
            "scythe", ModItems.ALLOY_SCYTHE, 1,
            // 右侧弯刃 2 锭 + 左侧长柄 3 木棍
            List.of(
                    new ActiveSlot(0, 52, 50, SlotType.INGOT),
                    new ActiveSlot(1, 77, 64, SlotType.INGOT),
                    new ActiveSlot(2, 22, 47, SlotType.HANDLE),
                    new ActiveSlot(3, 22, 77, SlotType.HANDLE),
                    new ActiveSlot(4, 22, 107, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_SCYTHE).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate PICKAXE = register(new CastingTemplate(
            "pickaxe", ModItems.ALLOY_PICKAXE, 1,
            List.of(
                    new ActiveSlot(0, 23, 57, SlotType.INGOT),
                    new ActiveSlot(1, 52, 49, SlotType.INGOT),
                    new ActiveSlot(2, 81, 58, SlotType.INGOT),
                    new ActiveSlot(3, 52, 78, SlotType.HANDLE),
                    new ActiveSlot(4, 52, 106, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_PICKAXE).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate HOE = register(new CastingTemplate(
            "hoe", ModItems.ALLOY_HOE, 1,
            // 右侧单锭锄头 + 左下手柄 2 木棍
            List.of(
                    new ActiveSlot(0, 35, 49, SlotType.INGOT),
                    new ActiveSlot(1, 74, 50, SlotType.INGOT),
                    new ActiveSlot(2, 39, 71, SlotType.HANDLE),
                    new ActiveSlot(3, 39, 91, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_HOE).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate SHOVEL = register(new CastingTemplate(
            "shovel", ModItems.ALLOY_SHOVEL, 1,
            // 1 锭 + 3 木棍
            List.of(
                    new ActiveSlot(0, 51, 46, SlotType.INGOT),
                    new ActiveSlot(1, 51, 68, SlotType.HANDLE),
                    new ActiveSlot(2, 51, 90, SlotType.HANDLE),
                    new ActiveSlot(3, 51, 112, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_SHOVEL).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate MATTOCK = register(new CastingTemplate(
            "mattock", ModItems.ALLOY_MATTOCK, 1,
            List.of(
                    new ActiveSlot(0, 16, 55, SlotType.INGOT),
                    new ActiveSlot(1, 49, 46, SlotType.INGOT),
                    new ActiveSlot(2, 81, 51, SlotType.INGOT),
                    new ActiveSlot(3, 81, 76, SlotType.INGOT),
                    new ActiveSlot(4, 49, 79, SlotType.HANDLE),
                    new ActiveSlot(5, 49, 110, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_MATTOCK).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate SHEARS = register(new CastingTemplate(
            "shears", ModItems.ALLOY_SHEARS, 1,
            // 5 粒（左右两片刀刃 + 铰接），无手柄
            List.of(
                    new ActiveSlot(0, 27, 59, SlotType.NUGGET),
                    new ActiveSlot(1, 73, 59, SlotType.NUGGET),
                    new ActiveSlot(2, 50, 85, SlotType.NUGGET),
                    new ActiveSlot(3, 35, 107, SlotType.NUGGET),
                    new ActiveSlot(4, 65, 107, SlotType.NUGGET)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_SHEARS).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    // ============================================================
    //  板甲（用户给定坐标）
    // ============================================================

    public static final CastingTemplate HELMET = register(new CastingTemplate(
            "helmet", ModItems.ALLOY_HELMET, 1,
            List.of(
                    new ActiveSlot(0, 20, 50, SlotType.INGOT),
                    new ActiveSlot(1, 44, 47, SlotType.INGOT),
                    new ActiveSlot(2, 68, 50, SlotType.INGOT),
                    new ActiveSlot(3, 17, 75, SlotType.INGOT),
                    new ActiveSlot(4, 71, 75, SlotType.INGOT)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_HELMET).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate CHESTPLATE = register(new CastingTemplate(
            "chestplate", ModItems.ALLOY_CHESTPLATE, 1,
            List.of(
                    new ActiveSlot(0, 17, 46, SlotType.INGOT),
                    new ActiveSlot(1, 83, 46, SlotType.INGOT),
                    new ActiveSlot(2, 25, 73, SlotType.INGOT),
                    new ActiveSlot(3, 51, 71, SlotType.INGOT),
                    new ActiveSlot(4, 77, 73, SlotType.INGOT),
                    new ActiveSlot(5, 25, 99, SlotType.INGOT),
                    new ActiveSlot(6, 77, 99, SlotType.INGOT),
                    new ActiveSlot(7, 51, 101, SlotType.INGOT)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_CHESTPLATE).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate LEGGINGS = register(new CastingTemplate(
            "leggings", ModItems.ALLOY_LEGGINGS, 1,
            List.of(
                    new ActiveSlot(0, 18, 50, SlotType.INGOT),
                    new ActiveSlot(1, 18, 75, SlotType.INGOT),
                    new ActiveSlot(2, 18, 100, SlotType.INGOT),
                    new ActiveSlot(3, 46, 52, SlotType.INGOT),
                    new ActiveSlot(4, 74, 50, SlotType.INGOT),
                    new ActiveSlot(5, 74, 75, SlotType.INGOT),
                    new ActiveSlot(6, 74, 100, SlotType.INGOT)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_LEGGINGS).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate BOOTS = register(new CastingTemplate(
            "boots", ModItems.ALLOY_BOOTS, 1,
            List.of(
                    new ActiveSlot(0, 21, 58, SlotType.INGOT),
                    new ActiveSlot(1, 70, 58, SlotType.INGOT),
                    new ActiveSlot(2, 18, 84, SlotType.INGOT),
                    new ActiveSlot(3, 74, 84, SlotType.INGOT)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_BOOTS).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate BODY = register(new CastingTemplate(
            "body", ModItems.ALLOY_BODY, 1,
            // 马铠：沿用胸甲布局（用户未单独给定，与板甲同形）
            List.of(
                    new ActiveSlot(0, 17, 46, SlotType.INGOT),
                    new ActiveSlot(1, 83, 46, SlotType.INGOT),
                    new ActiveSlot(2, 25, 73, SlotType.INGOT),
                    new ActiveSlot(3, 51, 71, SlotType.INGOT),
                    new ActiveSlot(4, 77, 73, SlotType.INGOT),
                    new ActiveSlot(5, 25, 99, SlotType.INGOT),
                    new ActiveSlot(6, 77, 99, SlotType.INGOT),
                    new ActiveSlot(7, 51, 101, SlotType.INGOT)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_BODY).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    // ============================================================
    //  锁链甲（与板甲同形，仅槽位类型换 NUGGET / CHAIN）
    // ============================================================

    public static final CastingTemplate CHAIN_HELMET = register(new CastingTemplate(
            "chain_helmet", ModItems.ALLOY_CHAIN_HELMET, 1,
            List.of(
                    new ActiveSlot(0, 20, 50, SlotType.CHAIN),
                    new ActiveSlot(1, 44, 47, SlotType.CHAIN),
                    new ActiveSlot(2, 68, 50, SlotType.CHAIN),
                    new ActiveSlot(3, 17, 75, SlotType.CHAIN),
                    new ActiveSlot(4, 71, 75, SlotType.CHAIN)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_CHAIN_HELMET).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate CHAIN_CHESTPLATE = register(new CastingTemplate(
            "chain_chestplate", ModItems.ALLOY_CHAIN_CHESTPLATE, 1,
            List.of(
                    new ActiveSlot(0, 17, 46, SlotType.CHAIN),
                    new ActiveSlot(1, 83, 46, SlotType.CHAIN),
                    new ActiveSlot(2, 25, 73, SlotType.CHAIN),
                    new ActiveSlot(3, 51, 71, SlotType.CHAIN),
                    new ActiveSlot(4, 77, 73, SlotType.CHAIN),
                    new ActiveSlot(5, 25, 99, SlotType.CHAIN),
                    new ActiveSlot(6, 77, 99, SlotType.CHAIN),
                    new ActiveSlot(7, 51, 101, SlotType.CHAIN)
            ),
            ModCastingTemplates::sumInputs,
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_CHAIN_CHESTPLATE).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate CHAIN_LEGGINGS = register(new CastingTemplate(
            "chain_leggings", ModItems.ALLOY_CHAIN_LEGGINGS, 1,
            List.of(
                    new ActiveSlot(0, 18, 50, SlotType.CHAIN),
                    new ActiveSlot(1, 18, 75, SlotType.CHAIN),
                    new ActiveSlot(2, 18, 100, SlotType.CHAIN),
                    new ActiveSlot(3, 46, 52, SlotType.CHAIN),
                    new ActiveSlot(4, 74, 50, SlotType.CHAIN),
                    new ActiveSlot(5, 74, 75, SlotType.CHAIN),
                    new ActiveSlot(6, 74, 100, SlotType.CHAIN)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_CHAIN_LEGGINGS).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate CHAIN_BOOTS = register(new CastingTemplate(
            "chain_boots", ModItems.ALLOY_CHAIN_BOOTS, 1,
            List.of(
                    new ActiveSlot(0, 21, 58, SlotType.CHAIN),
                    new ActiveSlot(1, 70, 58, SlotType.CHAIN),
                    new ActiveSlot(2, 18, 84, SlotType.CHAIN),
                    new ActiveSlot(3, 74, 84, SlotType.CHAIN)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_CHAIN_BOOTS).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    public static final CastingTemplate CHAIN_BODY = register(new CastingTemplate(
            "chain_body", ModItems.ALLOY_CHAIN_BODY, 1,
            // 锁链马铠：沿用胸甲布局 + CHAIN 类型
            List.of(
                    new ActiveSlot(0, 17, 46, SlotType.CHAIN),
                    new ActiveSlot(1, 83, 46, SlotType.CHAIN),
                    new ActiveSlot(2, 25, 73, SlotType.CHAIN),
                    new ActiveSlot(3, 51, 71, SlotType.CHAIN),
                    new ActiveSlot(4, 77, 73, SlotType.CHAIN),
                    new ActiveSlot(5, 25, 99, SlotType.CHAIN),
                    new ActiveSlot(6, 77, 99, SlotType.CHAIN),
                    new ActiveSlot(7, 51, 101, SlotType.CHAIN)
            ),
            ModCastingTemplates::sumInputs,
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModArmorItem) ModItems.ALLOY_CHAIN_BODY).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    // ============================================================
    //  铸造锤（用户给定坐标：2 锭 + 3 木棍）
    // ============================================================

    public static final CastingTemplate CASTING_HAMMER = register(new CastingTemplate(
            "casting_hammer", ModItems.ALLOY_CASTING_HAMMER, 1,
            List.of(
                    new ActiveSlot(0, 22, 59, SlotType.INGOT),
                    new ActiveSlot(1, 78, 59, SlotType.INGOT),
                    new ActiveSlot(2, 50, 48, SlotType.HANDLE),
                    new ActiveSlot(3, 50, 74, SlotType.HANDLE),
                    new ActiveSlot(4, 50, 100, SlotType.HANDLE)
            ),
            inputs -> sumInputs(inputs),
            (props, inputs, quality) -> {
                SynergyEffect q = ModSynergies.qualityFor(quality);
                AlloyComponent alloy = q != null ? props.withSynergy(q.id()) : props;
                ItemStack stack = ((ModToolItem) ModItems.ALLOY_CASTING_HAMMER).createFromAlloy(alloy);
                return applyQualityAndStamp(stack, props, quality);
            }
    ));

    // ============================================================
    //  工具箱（4 粒 + 2 木棍 + 2 皮革绳，顺序排列；产物按主导金属）
    // ============================================================

    public static final CastingTemplate TOOLBOX = register(new CastingTemplate(
            "toolbox", ModItems.COPPER_TOOLBOX, 1,
            // 3x3 九宫格顺序填 8 格：4 粒 → 2 木棍 → 2 皮革绳
            List.of(
                    new ActiveSlot(0, 30, 46, SlotType.NUGGET),
                    new ActiveSlot(1, 56, 46, SlotType.NUGGET),
                    new ActiveSlot(2, 82, 46, SlotType.NUGGET),
                    new ActiveSlot(3, 30, 72, SlotType.NUGGET),
                    new ActiveSlot(4, 56, 72, SlotType.HANDLE),
                    new ActiveSlot(5, 82, 72, SlotType.HANDLE),
                    new ActiveSlot(6, 30, 98, SlotType.SINEW),
                    new ActiveSlot(7, 56, 98, SlotType.SINEW)
            ),
            // 属性：只汇总金属粒（木棍 / 皮革绳不参与），剥掉形态共鸣
            inputs -> stripForm(sumInputs(inputs)),
            // 物品合成：按含量最高的金属（dominant）产出对应工具箱。
            // 工具箱只是"金属身份"的可合成载体，本身不带合金属性、也不吃品质。
            (props, inputs, quality) -> new ItemStack(ModItems.toolboxFor(props.dominant()))
    ));

    // ============================================================
    //  空桶（3 锭，U 形；产物按主导金属，与工具箱同构、不带合金属性）
    // ============================================================

    public static final CastingTemplate BUCKET = register(new CastingTemplate(
            "bucket", ModItems.COPPER_BUCKET, 1,
            List.of(
                    new ActiveSlot(0, 30, 46, SlotType.INGOT),
                    new ActiveSlot(1, 82, 46, SlotType.INGOT),
                    new ActiveSlot(2, 56, 72, SlotType.INGOT)
            ),
            inputs -> stripForm(sumInputs(inputs)),
            // 空桶只是容器，不带合金属性、不吃品质；水/岩浆/石桶由空桶右键流体得到
            (props, inputs, quality) -> new ItemStack(ModItems.bucketFor(props.dominant()))
    ));

    // ============================================================
    //  查询
    // ============================================================

    public static CastingTemplate byIndex(int index) {
        List<CastingTemplate> all = REGISTRY.stream().toList();
        return all.get(Math.floorMod(index, all.size()));
    }
}
