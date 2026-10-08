package com.mitenewworld.entity.blockentity.castingtable;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.block.castingtable.AbstractCastingTableBlock;
import com.mitenewworld.block.castingtable.CastingTableDefaults;
import com.mitenewworld.block.castingtable.ModCastingTableBlock;
import com.mitenewworld.core.ImplementedInventory;
import com.mitenewworld.registry.ModBlockEntities;
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
import com.mitenewworld.recipe.casting.CastingTemplate;
import com.mitenewworld.recipe.casting.CastingTemplate.ActiveSlot;
import com.mitenewworld.recipe.casting.ModCastingTemplates;
import com.mitenewworld.recipe.casting.SlotType;
import com.mitenewworld.screen.castingtable.ModCastingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ModCastingTableEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {

    public enum CastingState {
        IDLE, READY, FORGING, COMPLETED;

        public static CastingState byOrdinal(int i) {
            CastingState[] v = values();
            return (i >= 0 && i < v.length) ? v[i] : IDLE;
        }
    }

    public static final int CASTING_SLOTS = 16;
    public static final int OUTPUT_SLOT = 16;
    public static final int HAMMER_SLOT = 17;
    public static final int INVENTORY_SIZE = 18;

    public static final float TABLE_WORK_PER_LEVEL = 1000f;
    /** 单次敲击的基础进度（所有判定档都加）；优良(+1)/完美(+2) 再额外加 STRIKE_PROGRESS_BONUS */
    public static final int STRIKE_PROGRESS_BASE = 20;
    /** 判定档 1、2 在基础进度之上额外加快的进度 */
    public static final int STRIKE_PROGRESS_BONUS = 20;

    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    /** 同步到客户端的属性数量（GUI 进度/温度等） */
    public static final int PROPERTY_COUNT = 9;

    private final PropertyDelegate propertyDelegate;

    private int templateIndex = 0;
    private CastingState castingState = CastingState.IDLE;

    private int tableLevel = 1;
    private float currentDurability = 100f;
    private float maxDurability = 100f;

    /** 未被注入组件时的默认配比 */
    private static final Map<String, Float> DEFAULT_METALS = Map.of("iron", 1.0f);

    /** 该铸造台的金属配比（损坏掉落的废金属用它） */
    private Map<String, Float> metals = DEFAULT_METALS;

    private float totalWork = 0f;
    private float progress = 0f;
    private int qualityScore = 0;
    private float targetHardness = 0f;

    private UUID owner = null;

    public ModCastingTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MOD_CASTING_TABLE_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> templateIndex;
                    case 1 -> castingState.ordinal();
                    case 2 -> tableLevel;
                    case 3 -> (int) currentDurability;
                    case 4 -> (int) maxDurability;
                    case 5 -> (int) progress;
                    case 6 -> (int) totalWork;
                    case 7 -> qualityScore;
                    case 8 -> owner == null ? 0 : 1;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    templateIndex = value;
                } else if (index == 1) castingState = CastingState.byOrdinal(value);
            }

            @Override
            public int size() { return PROPERTY_COUNT; }
        };
    }

    public CastingTemplate template() { return ModCastingTemplates.byIndex(templateIndex); }
    public int templateIndex() { return templateIndex; }
    public PropertyDelegate getPropertyDelegate() { return propertyDelegate; }
    public CastingState castingState() { return castingState; }
    public int tableLevel() { return tableLevel; }
    public UUID owner() { return owner; }

    /** 是否正在被别的玩家占用（仅 FORGING 状态锁定） */
    public boolean isOccupiedByOther(PlayerEntity player) {
        return castingState == CastingState.FORGING
                && owner != null
                && (player == null || !owner.equals(player.getUuid()));
    }

    /** 读数快照（防止}}=null 时其他地方拿到半初始化状态） */
    public Map<String, Float> metals() { return metals; }

    /**
     * 金属铸造台被人用 /setblock 直接放置（没有经过物品组件注入）时，
     * 把配比 / 耐久补成该方块的默认金属，否则渲染会按 iron 上色。
     *
     * <p>只在配比仍然等于默认值时改写，避免覆盖玩家真正放下来的合金铸造台。
     */
    private void syncDefaultMetals() {
        if (!(getCachedState().getBlock() instanceof ModCastingTableBlock block)) {
            return;
        }
        String expected = block.metalId();
        if (!DEFAULT_METALS.equals(metals) || expected.equals(metalKey())) {
            return;
        }
        ModAlloyMetals.MetalType type = ModAlloyMetals.byId(expected);
        if (type == null) {
            return;
        }
        CastingTableComponent comp = CastingTableDefaults.componentFor(type);
        this.metals = Map.copyOf(comp.metals());
        this.maxDurability = comp.maxDurability();
        this.currentDurability = Math.min(comp.durability(), comp.maxDurability());
        markDirty();
    }

    /** 唯一金属 id；混合配比则返回 null */
    private String metalKey() {
        return metals.size() == 1 ? metals.keySet().iterator().next() : null;
    }
    public float maxDurability() { return maxDurability; }
    public float currentDurability() { return currentDurability; }
    public boolean isBroken() { return currentDurability <= 0f; }

    /** 方块/物品调色用：金属加权平均色 */
    public int metalColor() {
        return CastingTableComponent.colorOf(metals);
    }

    /** 放置时由物品组件注入 */
    public void applyFromComponent(CastingTableComponent comp) {
        this.metals = Map.copyOf(comp.metals());
        this.tableLevel = Math.max(1, comp.level());
        this.maxDurability = Math.max(1f, comp.maxDurability());
        this.currentDurability = Math.min(comp.durability(), maxDurability);
        markDirty();
    }

    @Override public DefaultedList<ItemStack> getItems() { return inventory; }
    @Override public int getMaxCountPerStack() { return 16; }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return pos;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ModCastingTableScreenHandler(syncId, inv, this, propertyDelegate, pos);
    }

    @Override
    public Text getDisplayName() {
        // 名称后括号显示铸造台等级，如：铸造台 (1)
        return Text.translatable("container.mitenewworld.casting_table")
                .append(Text.literal(" (" + tableLevel + ")"));
    }

    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view, inventory);
        view.putInt("template", templateIndex);
        view.putInt("state", castingState.ordinal());
        view.putInt("tableLevel", tableLevel);
        view.putFloat("curDur", currentDurability);
        view.putFloat("maxDur", maxDurability);
        view.putFloat("totalWork", totalWork);
        view.putFloat("progress", progress);
        view.putInt("quality", qualityScore);
        view.putFloat("targetHardness", targetHardness);
        view.putString("metals", encodeMetals(metals));
        if (owner != null) {
            view.putString("owner", owner.toString());
        }
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view, inventory);
        templateIndex = view.getInt("template", 0);
        castingState = CastingState.byOrdinal(view.getInt("state", 0));
        tableLevel = view.getInt("tableLevel", 1);
        currentDurability = view.getFloat("curDur", 100f);
        maxDurability = view.getFloat("maxDur", 100f);
        totalWork = view.getFloat("totalWork", 0f);
        progress = view.getFloat("progress", 0f);
        qualityScore = view.getInt("quality", 0);
        targetHardness = view.getFloat("targetHardness", 0f);
        String ownerStr = view.getString("owner", "");
        owner = ownerStr.isEmpty() ? null : UUID.fromString(ownerStr);
        Map<String, Float> loaded = decodeMetals(view.getString("metals", ""));
        if (!loaded.isEmpty()) {
            metals = loaded;
        }
    }

    private static String encodeMetals(Map<String, Float> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(k).append('=').append(v);
        });
        return sb.toString();
    }

    private static Map<String, Float> decodeMetals(String encoded) {
        Map<String, Float> out = new LinkedHashMap<>();
        if (encoded == null || encoded.isEmpty()) {
            return out;
        }
        for (String part : encoded.split(",")) {
            int i = part.indexOf('=');
            if (i <= 0) {
                continue;
            }
            try {
                out.put(part.substring(0, i), Float.parseFloat(part.substring(i + 1)));
            } catch (NumberFormatException ignored) {
            }
        }
        return out;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack old = getStack(slot).copy();
        ImplementedInventory.super.setStack(slot, stack);
        if (ItemStack.areItemsAndComponentsEqual(old, getStack(slot))) {
            return;
        }

        if (castingState == CastingState.FORGING) {
            resetProgress();
        } else if (slot == OUTPUT_SLOT && getStack(slot).isEmpty()) {
            castingState = CastingState.IDLE;
            if (canCraft()) {
                castingState = CastingState.READY;
            }
        } else {
            if (canCraft()) {
                castingState = CastingState.READY;
            } else if (castingState != CastingState.COMPLETED) castingState = CastingState.IDLE;
        }
        markDirty();
    }

    public void setTemplate(PlayerEntity player, int index) {
        int next = Math.floorMod(index, templateCount());
        if (next == templateIndex) {
            return;
        }

        for (int i = 0; i <= OUTPUT_SLOT; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                if (player != null) {
                    player.dropItem(stack.copy(), false);
                }
                inventory.set(i, ItemStack.EMPTY);
            }
        }

        templateIndex = next;
        resetProgress();
        markDirty();
    }

    /** 模板总数：必须按注册表实际大小取模，写死常量会导致翻页绕回来时重复前面的模板 */
    private int templateCount() {
        int size = ModCastingTemplates.REGISTRY.size();
        return size > 0 ? size : 1;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }

        syncDefaultMetals();

        if (owner != null && world.getServer() != null
                && world.getServer().getPlayerManager().getPlayer(owner) == null) {
            owner = null;
            markDirty();
        }

        if (castingState == CastingState.FORGING) {
            // 被动推进：每 tick 累加台子自身等级的进度。
            // 例：总时长 = 成品 maxDamage，2 级台每 tick +2 → 200 时长即 100 tick 打完；
            // 玩家敲击小游戏在此之上额外加速（见 applyMinigameResult）。
            progress += tableLevel;
            if (progress >= totalWork) {
                completeCraft(world);
            }
            markDirty();
            return;
        }

        if (castingState != CastingState.COMPLETED) {
            CastingState desired = canCraft() ? CastingState.READY : CastingState.IDLE;
            if (castingState != desired) {
                castingState = desired;
                markDirty();
            }
        }
    }

    public void onMinigameClick(ServerPlayerEntity player, int grade) {
        switch (castingState) {
            case READY -> startForging(player);
            case FORGING -> {
                if (owner == null || !owner.equals(player.getUuid())) {
                    return;
                }
                applyMinigameResult(world, grade);
            }
            default -> {}
        }
    }

    private void startForging(ServerPlayerEntity player) {
        if (!canCraft()) {
            return;
        }

        List<AlloyComponent> comps = collectComponents();
        if (comps == null || comps.isEmpty()) {
            return;
        }

        ItemStack preview = buildResult(template(), comps);
        if (preview.isEmpty()) {
            return;
        }

        this.totalWork = computeTotalWork(preview);
        this.progress = 0f;
        this.qualityScore = 0;
        this.targetHardness = computeTargetHardness(comps);
        this.castingState = CastingState.FORGING;
        this.owner = player.getUuid();
        markDirty();
    }

    private void applyMinigameResult(World world, int grade) {
        // 品质分：五档分别累加 +2/+1/0/-1/-2
        qualityScore += grade;
        // 进度：每次敲击都加速；优良(+1)/完美(+2) 在基础进度之上额外 +STRIKE_PROGRESS_BONUS。
        // 基础进度对所有档位一致，总时长由被动 tick（台子等级）与这些敲击共同推进。
        int strike = STRIKE_PROGRESS_BASE + (grade >= 1 ? STRIKE_PROGRESS_BONUS : 0);
        progress += strike;
        if (progress > totalWork) {
            progress = totalWork;
        }

        boolean hammerOk = consumeHammerDurability();
        if (!hammerOk) {
            castingState = CastingState.READY;
            progress = 0f;
            totalWork = 0f;
            qualityScore = 0;
            owner = null;
            markDirty();
            return;
        }

        if (progress >= totalWork) {
            completeCraft(world);
        }
        markDirty();
    }

    private void completeCraft(World world) {
        List<AlloyComponent> comps = collectComponents();
        if (comps == null || comps.isEmpty()) { resetProgress(); return; }

        ItemStack result = buildResult(template(), comps);
        if (result.isEmpty()) { resetProgress(); return; }

        for (ActiveSlot slot : template().activeSlots()) {
            ItemStack stack = inventory.get(slot.index());
            stack.decrement(1);
            if (stack.isEmpty()) {
                inventory.set(slot.index(), ItemStack.EMPTY);
            }
        }

        consumeTableDurability(world);

        inventory.set(OUTPUT_SLOT, result);
        castingState = CastingState.COMPLETED;
        progress = 0f;
        totalWork = 0f;
        qualityScore = 0;
        owner = null;
        markDirty();
    }

    private boolean consumeHammerDurability() {
        ItemStack hammer = inventory.get(HAMMER_SLOT);
        if (hammer.isEmpty()) {
            return false;
        }

        // 没有耐久模型的锤子（创造模式直接取出的、未走 createFromAlloy 的）不消耗，
        // 否则 maxDamage == 0 会让第一次敲击就判定为"打爆"，进度被清零。
        int maxDamage = hammer.getMaxDamage();
        if (maxDamage <= 0) {
            return true;
        }

        int itemTier = ModToolItem.MiningTier.of(targetHardness).ordinal();
        int hammerTier = getHammerTier(hammer);

        int cost = (itemTier <= hammerTier) ? 1 : 3 * (itemTier - hammerTier);
        int newDamage = hammer.getDamage() + cost;
        if (newDamage >= maxDamage) {
            inventory.set(HAMMER_SLOT, ItemStack.EMPTY);
            return false;
        }
        hammer.setDamage(newDamage);
        return true;
    }

    private void consumeTableDurability(World world) {
        int itemTier = ModToolItem.MiningTier.of(targetHardness).ordinal();
        int tableTier = tableLevel - 2;
        int cost = (itemTier <= tableTier) ? 1 : 10 * (itemTier - tableTier);
        currentDurability -= cost;

        if (currentDurability <= 0) {
            currentDurability = 0;
            replaceWithDamagedBlock(world);
        } else {
            updateDamageState(world);
        }
        markDirty();
    }

    private int getHammerTier(ItemStack hammer) {
        AlloyComponent comp = hammer.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return 0;
        }
        return ModToolItem.MiningTier.of(comp.hardness()).ordinal();
    }

    private void replaceWithDamagedBlock(World world) {
        BlockState state = getCachedState();
        if (state.getBlock() instanceof AbstractCastingTableBlock) {
            world.setBlockState(pos, state.with(AbstractCastingTableBlock.DAMAGE, 3), 3);
        }
    }

    /** 耐久变化时同步方块损坏档位（0 正常 ~ 3 完全损坏） */
    private void updateDamageState(World world) {
        float ratio = maxDurability > 0 ? currentDurability / maxDurability : 0f;
        BlockState state = getCachedState();
        if (!(state.getBlock() instanceof AbstractCastingTableBlock)) {
            return;
        }
        int damage = AbstractCastingTableBlock.damageFor(ratio);
        if (state.get(AbstractCastingTableBlock.DAMAGE) != damage) {
            world.setBlockState(pos, state.with(AbstractCastingTableBlock.DAMAGE, damage), 3);
        }
    }

    private boolean canCraft() {
        if (castingState == CastingState.FORGING) {
            return false;
        }
        if (!inventory.get(OUTPUT_SLOT).isEmpty()) {
            return false;
        }
        if (inventory.get(HAMMER_SLOT).isEmpty()) {
            return false;
        }

        List<AlloyComponent> comps = collectComponents();
        if (comps == null || comps.isEmpty()) {
            return false;
        }
        return AlloyComponent.canCombine(comps);
    }

    private List<AlloyComponent> collectComponents() {
        List<AlloyComponent> comps = new ArrayList<>();
        for (ActiveSlot slot : template().activeSlots()) {
            ItemStack stack = inventory.get(slot.index());
            if (stack.isEmpty()) {
                return null;
            }
            if (!matchesType(stack, slot.type())) {
                return null;
            }
            if (!slot.type().countsAsMetal()) {
                continue;
            }
            AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
            if (comp == null) {
                return null;
            }
            comps.add(comp);
        }
        return comps;
    }

    private static boolean matchesType(ItemStack stack, SlotType type) {
        return type.accepts(stack);
    }

    private float computeTargetHardness(List<AlloyComponent> comps) {
        return AlloyComponent.sumForEquipment(comps, AlloyComponent.averageMetals(comps)).hardness();
    }

    private float computeTotalWork(ItemStack preview) {
        int maxDamage = preview.getMaxDamage();
        if (maxDamage > 0) {
            return maxDamage;
        }
        return tableLevel * TABLE_WORK_PER_LEVEL;
    }

    private void resetProgress() {
        progress = 0f;
        totalWork = 0f;
        qualityScore = 0;
        owner = null;
        castingState = canCraft() ? CastingState.READY : CastingState.IDLE;
    }

    /** 合成时只按模版 apply（逻辑都在模版里） */
    private ItemStack buildResult(CastingTemplate template, List<AlloyComponent> comps) {
        return template.craft(comps, qualityScore);
    }
}
