package com.mitenewworld.entity.blockentity.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.block.alloyfurnace.BlastFurnaceStructure;
import com.mitenewworld.core.FuelMap;
import com.mitenewworld.core.ImplementedInventory;
import com.mitenewworld.data.ModBlastFurnaceBlockData;
import com.mitenewworld.registry.ModItems;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.component.OreComponent;
import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.AlloyItem;
import com.mitenewworld.item.metal.ModAlloyMetals;
import com.mitenewworld.item.metal.ModAlloyMetals.MetalType;
import com.mitenewworld.screen.alloyfurnace.ModBlastFurnaceScreenHandler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventories;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractModBlastFurnaceEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory<ModBlastFurnaceBlockData>, ImplementedInventory {

    public static final int[] INPUT_SLOTS       = {1, 5, 9, 13, 21};
    /** 设计温度（℃）：玩家意图的工作温度，最高温度 = 设计温度 ÷ 0.8 */
    public static final int[] DESIGN_TEMPERATURE  = {400, 800, 1200, 1600, 2000};
    /** 最高温度（℃）：温度超过即熔化 */
    public static final int[] MAX_TEMPERATURE   = {500, 1000, 1500, 2000, 2500};
    /** 熔炼速度：达到工作温度时每 tick 提供的热（保留原 gating 逻辑） */
    public static final int[] MAX_HEAT_PER_TICK = {5, 20, 60, 180, 500};
    /** 热值（燃料）容量上限 */
    public static final int MAX_HEAT_CAPACITY = 12800;
    /** 每 tick 热值消耗 */
    public static final int HEAT_LOSS_PER_TICK = 1;
    /** 温度每多少 tick 变化 1 度 */
    public static final int HEAT_RISE_TICKS = 20;
    /** 有热值时每周期升温度数 */
    public static final int HEAT_RISE_PER_CYCLE = 1;
    /** 无热值时每周期降温度数 */
    public static final int HEAT_FALL_PER_CYCLE = 1;
    /** 有热值时每多少 tick 尝试燃烧一次 */
    public static final int BURN_INTERVAL_TICKS = 100;
    /** 温度超过最高温度后多少 tick 触发熔化 */
    public static final int MELTDOWN_TICKS = 100;

    protected static final int MARK_DIRTY_INTERVAL   = 20;
    protected static final int HOPPER_PULL_INTERVAL  = 20;

    protected static final AlloyComponent SCRAP_HEAT_REF =
            AlloyComponent.compute(Map.of("iron", 1f), Set.of());

    protected final int tier;
    protected final int inputSlotCount;
    protected final int maxTemperature;
    protected final int maxHeatPerTick;

    protected DefaultedList<ItemStack> inventory;
    protected final PropertyDelegate propertyDelegate;

    protected int temperature = 0;
    /** 热值（燃料）：0 ~ MAX_HEAT_CAPACITY，每 tick -1 */
    protected int heatValue = 0;
    /** 热值容量（固定为 MAX_HEAT_CAPACITY） */
    protected int maxHeatCapacity = MAX_HEAT_CAPACITY;
    protected int cookProgress = 0;
    protected int maxCookProgress = 0;
    protected int craftState = 0;   // 0=待机,1=加热中,2=熔炼中,4=融化警告
    protected int meltdownTimer = 0;
    /** 温度变化的 tick 累加器 */
    private int heatCycleCounter = 0;
    /** 有热值时燃烧尝试的 tick 累加器 */
    private int burnCooldown = 0;

    protected boolean inputDirty = false;
    protected boolean fuelSlotDirty = true;
    protected boolean fuelSlotHasValidFuel = false;
    protected boolean fuelSlotHasExtinguisher = false;

    protected BlockPos hopperPos;
    protected BlockPos outputPos;

    protected int markDirtyCounter = 0;
    protected int hopperPullCounter = 0;
    /** 内部空腔是否已填充岩浆（只在 craftState == 2 时为 true） */
    protected boolean lavaFilled = false;

    protected enum Mode { FUSION, FLUX, SCRAP }
    protected Mode cachedMode = Mode.FUSION;
    @Nullable protected AlloyComponent cachedAlloy;
    @Nullable protected OreComponent cachedScrap;
    protected int cachedOutputCount = 0;
    /** 本次熔炼的累积锭当量（含小数），锭 / 粒拆分以此为唯一依据 */
    protected float cachedOutputUnits = 0f;
    protected float cachedTotalHeat = 0f;
    protected int cachedRequiredHeat = 0;
    protected boolean cachedLocked = false;

    public AbstractModBlastFurnaceEntity(BlockEntityType<?> type, BlockPos pos,
                                         BlockState state, int tier,
                                         BlockPos hopperPos, BlockPos outputPos) {
        super(type, pos, state);
        this.tier = MathHelper.clamp(tier, 1, 5);
        this.inputSlotCount  = INPUT_SLOTS[this.tier - 1];
        this.maxTemperature  = MAX_TEMPERATURE[this.tier - 1];
        this.maxHeatPerTick  = MAX_HEAT_PER_TICK[this.tier - 1];
        this.hopperPos = hopperPos;
        this.outputPos = outputPos;
        this.inventory = DefaultedList.ofSize(inputSlotCount + 2, ItemStack.EMPTY);

        this.propertyDelegate = new PropertyDelegate() {
            @Override public int get(int index) {
                return switch (index) {
                    case 0 -> temperature;
                    case 1 -> maxTemperature;
                    case 2 -> cookProgress;
                    case 3 -> maxCookProgress;
                    case 4 -> craftState;
                    case 5 -> heatValue;
                    case 6 -> maxHeatCapacity;
                    default -> 0;
                };
            }
            @Override public void set(int index, int value) {
                switch (index) {
                    case 0 -> temperature = value;
                    case 2 -> cookProgress = value;
                    case 3 -> maxCookProgress = value;
                    case 4 -> craftState = value;
                    case 5 -> heatValue = value;
                    case 6 -> maxHeatCapacity = value;
                }
            }
            @Override public int size() { return 7; }
        };
    }

    public abstract int getTierColor();

    public int getTier() { return tier; }
    public int getInputSlotCount() { return inputSlotCount; }
    public int getFuelSlotIndex()   { return inputSlotCount; }
    public int getFluxSlotIndex()   { return inputSlotCount + 1; }
    public PropertyDelegate getPropertyDelegate() { return propertyDelegate; }

    /** “在熔炼合金”状态 */
    public boolean isSmelting() { return craftState == 2; }

    @Override public DefaultedList<ItemStack> getItems() { return inventory; }

    @Override public ModBlastFurnaceBlockData getScreenOpeningData(ServerPlayerEntity player) {
        return new ModBlastFurnaceBlockData(pos, getTier());
    }

    @Override public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ModBlastFurnaceScreenHandler(syncId, inv, this, propertyDelegate, getTier());
    }

    @Override public Text getDisplayName() { return Text.translatable("container.blast_furnace"); }
    @Override public int getMaxCountPerStack() { return 81; }

    // ============================================================
    //  库存钩子
    // ============================================================

    @Override
    public void setStack(int slot, ItemStack stack) {
        ImplementedInventory.super.setStack(slot, stack);
        if (isInputOrFluxSlot(slot)) {
            inputDirty = true;
        }
        if (slot == getFuelSlotIndex()) {
            fuelSlotDirty = true;
        }
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack r = ImplementedInventory.super.removeStack(slot, amount);
        if (!r.isEmpty()) {
            if (isInputOrFluxSlot(slot)) {
                inputDirty = true;
            }
            if (slot == getFuelSlotIndex()) {
                fuelSlotDirty = true;
            }
        }
        return r;
    }

    protected boolean isInputOrFluxSlot(int slot) {
        return slot < inputSlotCount || slot == getFluxSlotIndex();
    }

    /** 析金粉槽只接受析金粉，避免漏斗把杂物塞进去被白白烧掉 */
    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if (slot == getFluxSlotIndex()) {
            return stack.isOf(ModItems.REFINING_POWDER);
        }
        return true;
    }

    /** 析出模式是否生效（槽内是析金粉且未耗尽） */
    protected boolean isFluxActive() {
        ItemStack flux = inventory.get(getFluxSlotIndex());
        return !flux.isEmpty() && flux.isOf(ModItems.REFINING_POWDER);
    }

    // ============================================================
    //  掉落
    // ============================================================

    /**
     * 掉落库存。
     * 只有 {@link #isSmelting()} 为 true 时清空不掉落，其他时间全部掉落
     * （含燃料和析金粉）。
     */
    public void dropContents(World world, BlockPos pos) {
        if (isSmelting()) {
            inventory.replaceAll(ignored -> ItemStack.EMPTY);
            return;
        }
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                inventory.set(i, ItemStack.EMPTY);
            }
        }
    }

    // ============================================================
    //  内部岩浆（3×3×3）
    // ============================================================

    protected List<BlockPos> getInteriorPositions(BlockState state) {
        if (!state.contains(net.minecraft.state.property.Properties.HORIZONTAL_FACING)) {
            return List.of();
        }
        Direction facing = state.get(net.minecraft.state.property.Properties.HORIZONTAL_FACING);
        Direction right = facing.rotateYClockwise();
        Direction back  = facing.getOpposite();
        Direction up    = Direction.UP;

        BlockPos origin = pos
                .offset(right.getOpposite(), 2)
                .offset(up.getOpposite(), 1);

        List<BlockPos> result = new ArrayList<>(27);
        for (int sz = 1; sz <= 3; sz++) {
            for (int sy = 1; sy <= 3; sy++) {
                for (int sx = 1; sx <= 3; sx++) {
                    result.add(origin.offset(right, sx).offset(up, sy).offset(back, sz));
                }
            }
        }
        return result;
    }

    protected void fillInteriorLava(World world, BlockState state) {
        for (BlockPos p : getInteriorPositions(state)) {
            if (world.getBlockState(p).isAir()) {
                world.setBlockState(p, Blocks.LAVA.getDefaultState());
            }
        }
    }

    protected void clearInteriorLava(World world, BlockState state) {
        for (BlockPos p : getInteriorPositions(state)) {
            if (world.getBlockState(p).isOf(Blocks.LAVA)) {
                world.setBlockState(p, Blocks.AIR.getDefaultState());
            }
        }
    }

    // ============================================================
    //  持久化
    // ============================================================

    @Override public void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view, inventory);
        view.putInt("temperature", temperature);
        view.putInt("heat_value", heatValue);
        view.putInt("max_heat_capacity", maxHeatCapacity);
        view.putInt("cook_progress", cookProgress);
        view.putInt("max_cook_progress", maxCookProgress);
        view.putInt("craft_state", craftState);
        view.putInt("meltdown_timer", meltdownTimer);
        view.putBoolean("lava_filled", lavaFilled);
        if (hopperPos != null) {
            view.putInt("hopper_x", hopperPos.getX());
            view.putInt("hopper_y", hopperPos.getY());
            view.putInt("hopper_z", hopperPos.getZ());
        }
        if (outputPos != null) {
            view.putInt("output_x", outputPos.getX());
            view.putInt("output_y", outputPos.getY());
            view.putInt("output_z", outputPos.getZ());
        }
    }

    @Override protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view, inventory);
        temperature      = view.getInt("temperature", 0);
        heatValue        = view.getInt("heat_value", 0);
        maxHeatCapacity  = view.getInt("max_heat_capacity", MAX_HEAT_CAPACITY);
        cookProgress     = view.getInt("cook_progress", 0);
        maxCookProgress = view.getInt("max_cook_progress", 0);
        craftState      = view.getInt("craft_state", 0);
        meltdownTimer   = view.getInt("meltdown_timer", 0);
        lavaFilled      = view.getBoolean("lava_filled", false);
        int hx = view.getInt("hopper_x", Integer.MIN_VALUE);
        if (hx != Integer.MIN_VALUE) {
            hopperPos = new BlockPos(hx, view.getInt("hopper_y", 0), view.getInt("hopper_z", 0));
        }
        int ox = view.getInt("output_x", Integer.MIN_VALUE);
        if (ox != Integer.MIN_VALUE) {
            outputPos = new BlockPos(ox, view.getInt("output_y", 0), view.getInt("output_z", 0));
        }
        inputDirty = true;
        fuelSlotDirty = true;
        cachedLocked = false;
    }

    // ============================================================
    //  Tick
    // ============================================================

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }

        // 1. 输入处理：只在温度 == 0 且输入脏时评估一次
        if (temperature == 0 && inputDirty) {
            inputDirty = false;
            clearCache();
            if (hasValidInput()) {
                buildCache();
                if (cachedAlloy != null || cachedScrap != null) {
                    cachedRequiredHeat = (cachedAlloy != null)
                            ? requiredHeatPerTick(cachedAlloy)
                            : requiredHeatPerTick(SCRAP_HEAT_REF);
                    cachedLocked = true;
                }
            }
        }

        // 2. 热值：每 tick -1
        if (heatValue > 0) {
            heatValue = Math.max(0, heatValue - HEAT_LOSS_PER_TICK);
        }

        // 3. 温度：每 20 tick ±1 度（有热值升温，无热值降温）
        if (++heatCycleCounter >= HEAT_RISE_TICKS) {
            heatCycleCounter = 0;
            if (heatValue > 0) {
                temperature += HEAT_RISE_PER_CYCLE;
            } else {
                temperature -= HEAT_FALL_PER_CYCLE;
            }
            if (temperature < 0) {
                temperature = 0;
            }
            // 上限留余量，防止溢出（熔化以 meltdownTimer 触发）
            if (temperature > maxTemperature * 2) {
                temperature = maxTemperature * 2;
            }
        }

        // 4. 熔化判定：温度 > 最高温度
        if (temperature > maxTemperature) {
            meltdownTimer++;
            craftState = 4;
            if (meltdownTimer >= MELTDOWN_TICKS) { meltFurnace(world, pos); return; }
        } else {
            meltdownTimer = 0;
            if (craftState == 4) {
                craftState = 0;
            }
        }

        // 5. 冶炼
        if (temperature > 0 && cachedLocked) {
            int currentHeat = (int) (maxHeatPerTick * (temperature / (float) maxTemperature));
            if (currentHeat < cachedRequiredHeat) {
                craftState = 1;
                cookProgress = 0;
                maxCookProgress = 0;
            } else {
                craftState = 2;
                maxCookProgress = Math.max(1, (int) Math.ceil(cachedTotalHeat / currentHeat));
                cookProgress++;
                if (cookProgress >= maxCookProgress) {
                    craftItem(world, state);
                }
            }
        } else {
            craftState = 0;
            cookProgress = 0;
            maxCookProgress = 0;
        }

        // 6. 岩浆：只在 craftState == 2 时保持，进入时填、离开时清
        if (craftState == 2) {
            if (!lavaFilled) {
                fillInteriorLava(world, state);
                lavaFilled = true;
            }
        } else {
            if (lavaFilled) {
                clearInteriorLava(world, state);
                lavaFilled = false;
            }
        }

        // 7. 燃料 / 灭火
        if (fuelSlotDirty) {
            fuelSlotDirty = false;
            ItemStack fuel = inventory.get(getFuelSlotIndex());
            fuelSlotHasValidFuel = !fuel.isEmpty()
                    && FuelMap.accepts(getTier(), fuel.getItem());
            fuelSlotHasExtinguisher = !fuel.isEmpty() && FuelMap.isExtinguisher(fuel.getItem());
        }
        // 灭火：有热值时水碗/水桶扣热值并返还空容器
        if (fuelSlotHasExtinguisher && heatValue > 0) {
            tryExtinguish(world, pos);
        }
        // 燃烧：有热值时每 100 tick 尝试一次（无热值仅由打火石 ignite 点燃）
        if (heatValue > 0) {
            if (++burnCooldown >= BURN_INTERVAL_TICKS) {
                burnCooldown = 0;
                if (fuelSlotHasValidFuel) {
                    tryBurn(world, pos);
                }
            }
        } else {
            burnCooldown = 0;
        }

        // 8. 漏斗吸取：每 20 tick 一次
        if (++hopperPullCounter >= HOPPER_PULL_INTERVAL) {
            hopperPullCounter = 0;
            pullFromHopper(world);
        }

        // 9. LIT 状态
        updateLitState(world, pos, state);

        // 10. markDirty 节流
        if (++markDirtyCounter >= MARK_DIRTY_INTERVAL) {
            markDirtyCounter = 0;
            markDirty(world, pos, state);
        }
    }

    protected void updateLitState(World world, BlockPos pos, BlockState state) {
        boolean lit = temperature > 0;
        if (state.get(AbstractFurnaceBlock.LIT) != lit) {
            world.setBlockState(pos, state.with(AbstractFurnaceBlock.LIT, lit), Block.NOTIFY_ALL);
        }
    }

    // ============================================================
    //  漏斗吸取
    // ============================================================

    protected void pullFromHopper(World world) {
        if (hopperPos == null) {
            return;
        }
        BlockEntity be = world.getBlockEntity(hopperPos);
        if (!(be instanceof Inventory inv)) {
            return;
        }

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (parseInput(stack) == null) {
                continue;
            }

            int target = findInputSlotFor(stack);
            if (target < 0) {
                continue;
            }

            ItemStack existing = inventory.get(target);
            if (existing.isEmpty()) {
                inventory.set(target, stack.split(1));
            } else {
                existing.increment(1);
                stack.decrement(1);
            }
            inv.markDirty();
            inputDirty = true;
            markDirty();
            return; // 每周期只吸一个
        }
    }

    /** 优先合并到同类未满的输入槽，其次放入空输入槽。 */
    protected int findInputSlotFor(ItemStack stack) {
        for (int i = 0; i < inputSlotCount; i++) {
            ItemStack s = inventory.get(i);
            if (!s.isEmpty()
                    && ItemStack.areItemsAndComponentsEqual(s, stack)
                    && s.getCount() < s.getMaxCount()) {
                return i;
            }
        }
        for (int i = 0; i < inputSlotCount; i++) {
            if (inventory.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    // ============================================================
    //  输入解析
    // ============================================================

    protected record InputContribution(Map<String, Float> metals, float units, Kind kind) {
        enum Kind { ALLOY, RAW_ORE, SCRAP }
    }

    @Nullable
    protected static InputContribution parseInput(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        OreComponent ore = stack.get(ModDataComponentTypes.ORE_COMPONENT);
        if (ore != null) {
            int count = stack.getCount();
            Map<String, Float> scaled = new HashMap<>();
            float total = 0f;
            for (var e : ore.metals().entrySet()) {
                float v = e.getValue() * count;
                scaled.put(e.getKey(), v);
                total += v;
            }
            InputContribution.Kind k = ore.kind() == OreComponent.Kind.SCRAP
                    ? InputContribution.Kind.SCRAP : InputContribution.Kind.RAW_ORE;
            return new InputContribution(scaled, total, k);
        }

        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp != null) {
            float perItem = formUnitFactor(comp);
            if (perItem <= 0f) {
                return null;
            }
            float totalUnits = perItem * stack.getCount();
            Map<String, Float> out = new HashMap<>();
            for (var e : comp.metals().entrySet()) {
                out.put(e.getKey(), e.getValue() * totalUnits);
            }
            return new InputContribution(out, totalUnits, InputContribution.Kind.ALLOY);
        }

        return null;
    }

    /**
     * 单个合金物品折算的锭当量。
     * 形态共鸣在组件里存的是短 id（nugget / block / chain），这里同时兼容带命名空间的写法。
     */
    private static float formUnitFactor(AlloyComponent comp) {
        Set<String> forced = comp.forcedSynergies();
        if (hasSynergy(forced, "block")) {
            return 9f;
        }
        if (hasSynergy(forced, "chain")) {
            return 0.45f;
        }
        if (hasSynergy(forced, "nugget")) {
            return 0.11f;
        }
        return 1f;
    }

    private static boolean hasSynergy(Set<String> forced, String id) {
        return forced.contains(id) || forced.contains(MITENewWorld.MOD_ID + ":" + id);
    }

    public static boolean isValidInput(ItemStack stack) { return parseInput(stack) != null; }

    // ============================================================
    //  缓存构建
    // ============================================================

    protected void buildCache() {
        if (isFluxActive()) {
            buildFluxCache();
        } else {
            buildFusionCache();
        }
    }

    private void buildFusionCache() {
        Map<String, Float> units = new HashMap<>();
        boolean hasRaw = false, hasScrap = false, hasAlloy = false;
        Set<String> rawMetals = new HashSet<>();

        for (int i = 0; i < inputSlotCount; i++) {
            InputContribution c = parseInput(inventory.get(i));
            if (c == null) {
                continue;
            }
            switch (c.kind()) {
                case RAW_ORE -> { hasRaw = true; if (c.metals().size() == 1) {
                    rawMetals.add(c.metals().keySet().iterator().next());
                } }
                case SCRAP -> hasScrap = true;
                case ALLOY -> hasAlloy = true;
            }
            for (var e : c.metals().entrySet()) {
                units.merge(e.getKey(), e.getValue(), Float::sum);
            }
        }
        if (units.isEmpty()) {
            return;
        }

        boolean valid = (hasRaw && !hasScrap && !hasAlloy && rawMetals.size() == 1)
                || (!hasRaw && !hasScrap && hasAlloy);
        if (!valid) { setScrapCache(units); return; }

        float totalUnits = sum(units);
        Map<String, Float> ratios = new HashMap<>();
        for (var e : units.entrySet()) {
            ratios.put(e.getKey(), e.getValue() / totalUnits);
        }

        cachedMode = Mode.FUSION;
        cachedAlloy = AlloyComponent.compute(ratios, Set.of());
        cachedScrap = null;
        cachedOutputUnits = totalUnits;
        cachedOutputCount = (int) Math.floor(totalUnits + 1e-4f);
        cachedTotalHeat = totalHeat(cachedAlloy) * cachedOutputUnits;
    }

    private void buildFluxCache() {
        Map<String, Float> units = new HashMap<>();
        boolean hasRaw = false, hasScrap = false, hasAlloy = false;
        Set<String> rawMetals = new HashSet<>();
        Set<String> scrapMetals = new HashSet<>();

        for (int i = 0; i < inputSlotCount; i++) {
            InputContribution c = parseInput(inventory.get(i));
            if (c == null) {
                continue;
            }
            switch (c.kind()) {
                case RAW_ORE -> { hasRaw = true; if (c.metals().size() == 1) {
                    rawMetals.add(c.metals().keySet().iterator().next());
                } }
                case SCRAP -> { hasScrap = true; c.metals().keySet().forEach(scrapMetals::add); }
                case ALLOY -> hasAlloy = true;
            }
            for (var e : c.metals().entrySet()) {
                units.merge(e.getKey(), e.getValue(), Float::sum);
            }
        }
        if (units.isEmpty()) {
            return;
        }

        if (hasRaw) {
            boolean valid = !hasScrap && !hasAlloy && rawMetals.size() == 1;
            if (!valid) { setScrapCache(units); return; }
            float totalUnits = sum(units);
            Map<String, Float> ratios = new HashMap<>();
            for (var e : units.entrySet()) {
                ratios.put(e.getKey(), e.getValue() / totalUnits);
            }
            cachedMode = Mode.FLUX;
            cachedAlloy = AlloyComponent.compute(ratios, Set.of());
            cachedScrap = null;
            cachedOutputUnits = totalUnits;
            cachedOutputCount = (int) Math.floor(totalUnits + 1e-4f);
            cachedTotalHeat = totalHeat(cachedAlloy) * cachedOutputUnits;
            return;
        }

        if (!hasAlloy && hasScrap && scrapMetals.size() > 1) { setScrapCache(units); return; }

        String dominant = dominantOf(units);
        if (dominant == null) {
            return;
        }
        float dominantUnits = units.get(dominant);
        cachedMode = Mode.FLUX;
        cachedAlloy = AlloyComponent.compute(Map.of(dominant, 1.0f), Set.of());
        cachedScrap = null;
        cachedOutputUnits = dominantUnits;
        cachedOutputCount = (int) Math.floor(dominantUnits + 1e-4f);
        cachedTotalHeat = totalHeat(cachedAlloy) * cachedOutputUnits;
    }

    private void setScrapCache(Map<String, Float> units) {
        String dominant = dominantOf(units);
        if (dominant == null) {
            cachedMode = Mode.SCRAP;
            cachedAlloy = null;
            cachedScrap = OreComponent.scrap(new LinkedHashMap<>(Map.of("iron", 1f)));
            cachedOutputCount = 1;
            cachedOutputUnits = 1f;
            cachedTotalHeat = 0f;
            return;
        }
        float total = sum(units);
        float dominantUnits = units.get(dominant);
        int count = Math.max(1, (int) Math.floor(dominantUnits));

        Map<String, Float> residual = new LinkedHashMap<>();
        for (var e : units.entrySet()) {
            float ratio = e.getValue() / total;
            residual.put(e.getKey(), ratio * (dominantUnits / count));
        }
        cachedMode = Mode.SCRAP;
        cachedAlloy = null;
        cachedScrap = OreComponent.scrap(residual);
        cachedOutputCount = count;
        cachedOutputUnits = dominantUnits;
        cachedTotalHeat = 100f * count;
    }

    protected void clearCache() {
        cachedAlloy = null;
        cachedScrap = null;
        cachedOutputCount = 0;
        cachedOutputUnits = 0f;
        cachedTotalHeat = 0f;
        cachedRequiredHeat = 0;
        cachedLocked = false;
        cookProgress = 0;
        maxCookProgress = 0;
    }

    // ============================================================
    //  合金参数
    // ============================================================

    protected float totalHeat(AlloyComponent comp) {
        float heat = 0f;
        for (var e : comp.metals().entrySet()) {
            MetalType m = ModAlloyMetals.byId(e.getKey());
            if (m != null) {
                heat += m.totalHeat() * e.getValue();
            }
        }
        return heat;
    }

    protected int requiredHeatPerTick(AlloyComponent comp) {
        double sumTier = 0;
        for (var e : comp.metals().entrySet()) {
            MetalType m = ModAlloyMetals.byId(e.getKey());
            if (m != null) {
                sumTier += m.requiredHeat().tier * e.getValue();
            }
        }
        int alloyTier = (int) Math.ceil(sumTier);
        MetalType dominant = ModAlloyMetals.byId(comp.dominant());
        if (dominant != null && comp.metals().getOrDefault(comp.dominant(), 0f) >= 0.5f) {
            alloyTier = Math.max(alloyTier, dominant.requiredHeat().tier);
        }
        return switch (alloyTier) {
            case 0 -> 5; case 1 -> 20; case 2 -> 60; case 3 -> 180; default -> 500;
        };
    }

    // ============================================================
    //  产出
    // ============================================================

    /**
     * 熔炼产出（矿物系统 §6）：
     * <pre>
     *   1.0 = 1 锭
     *   余数 × 9 = 粒数（向下取整）
     *   再余下的舍去
     * </pre>
     * 例：1.0 → 1 锭；0.5 → 4 粒；1.7 → 1 锭 + 6 粒；2.4 → 2 锭 + 3 粒。
     * 废金属模式不走锭 / 粒拆分，仍按“锭当量”成整数个废金属产出。
     */
    protected List<ItemStack> buildOutputs() {
        List<ItemStack> outputs = new ArrayList<>(2);

        if (cachedMode == Mode.SCRAP && cachedScrap != null) {
            ItemStack scrap = new ItemStack(ModItems.SCRAP_METAL);
            scrap.set(ModDataComponentTypes.ORE_COMPONENT, cachedScrap);
            scrap.setCount(Math.max(1, cachedOutputCount));
            outputs.add(scrap);
            return outputs;
        }

        if (cachedAlloy == null) {
            return outputs;
        }

        int ingots = (int) Math.floor(cachedOutputUnits + 1e-4f);
        int nuggets = (int) Math.floor((cachedOutputUnits - ingots) * 9f + 1e-4f);
        if (ingots <= 0 && nuggets <= 0) {
            if (cachedOutputUnits <= 0f) {
                return outputs;
            }
            // 不足 1 粒的残渣：保底给 1 粒，避免投入物白烧且永不产出
            nuggets = 1;
        }

        if (ingots > 0) {
            ItemStack ingot = AlloyItem.createFromComponent(ModItems.ALLOY_INGOT, AlloyItem.Form.INGOT, cachedAlloy);
            ingot.setCount(Math.min(ingots, ingot.getMaxCount()));
            outputs.add(ingot);
        }
        if (nuggets > 0) {
            ItemStack nugget = AlloyItem.derive(ModItems.ALLOY_NUGGET, AlloyItem.Form.NUGGET, cachedAlloy);
            nugget.setCount(Math.min(nuggets, nugget.getMaxCount()));
            outputs.add(nugget);
        }
        return outputs;
    }

    protected void tryOutput(World world, ItemStack output) {
        if (output.isEmpty()) {
            return;
        }
        BlockEntity be = world.getBlockEntity(outputPos);
        if (be instanceof Inventory inv) {
            ItemStack remaining = insertInto(inv, output);
            if (!remaining.isEmpty()) {
                spawnOutputItem(world, outputPos, remaining);
            }
            return;
        }
        spawnOutputItem(world, outputPos, output);
    }

    protected ItemStack insertInto(Inventory inv, ItemStack stack) {
        ItemStack remaining = stack.copy();
        boolean changed = false;
        for (int i = 0; i < inv.size() && !remaining.isEmpty(); i++) {
            ItemStack slot = inv.getStack(i);
            if (!slot.isEmpty()
                    && ItemStack.areItemsAndComponentsEqual(slot, remaining)
                    && slot.getCount() < slot.getMaxCount()) {
                int move = Math.min(remaining.getCount(), slot.getMaxCount() - slot.getCount());
                slot.increment(move);
                remaining.decrement(move);
                changed = true;
            }
        }
        for (int i = 0; i < inv.size() && !remaining.isEmpty(); i++) {
            if (inv.getStack(i).isEmpty()) {
                inv.setStack(i, remaining.copy());
                remaining.setCount(0);
                changed = true;
            }
        }
        if (changed) {
            inv.markDirty();
        }
        return remaining;
    }

    protected void spawnOutputItem(World world, BlockPos spawnPos, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        world.spawnEntity(new ItemEntity(world, spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5, stack));
    }

    protected void craftItem(World world, BlockState state) {
        List<ItemStack> outputs = buildOutputs();
        if (outputs.isEmpty()) {
            return;
        }

        for (ItemStack output : outputs) {
            tryOutput(world, output);
        }

        // 每次熔炼消耗 1 份析金粉（槽位已限定只能放析金粉）
        ItemStack flux = inventory.get(getFluxSlotIndex());
        if (!flux.isEmpty() && flux.isOf(ModItems.REFINING_POWDER)) {
            flux.decrement(1);
            if (flux.isEmpty()) {
                inventory.set(getFluxSlotIndex(), ItemStack.EMPTY);
            }
        }
        for (int i = 0; i < inputSlotCount; i++) {
            inventory.set(i, ItemStack.EMPTY);
        }

        clearCache();
        inputDirty = false;
        markDirty(world, pos, world.getBlockState(pos));
    }

    // ============================================================
    //  辅助
    // ============================================================

    private static float sum(Map<String, Float> units) {
        float s = 0f;
        for (float v : units.values()) {
            s += v;
        }
        return s;
    }

    @Nullable
    private static String dominantOf(Map<String, Float> units) {
        String best = null; float max = 0f;
        for (var e : units.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); best = e.getKey(); }
        }
        return best;
    }

    // ============================================================
    //  燃料 & 融化
    // ============================================================

    /** 打火石点火：无热值时尝试燃烧一次（点亮炉子） */
    public void ignite(World world, BlockPos pos) {
        if (heatValue > 0) {
            return;
        }
        ItemStack fuel = inventory.get(getFuelSlotIndex());
        if (!fuel.isEmpty() && FuelMap.accepts(getTier(), fuel.getItem())) {
            tryBurn(world, pos);
        }
    }

    protected void tryBurn(World world, BlockPos pos) {
        ItemStack fuel = inventory.get(getFuelSlotIndex());
        int burn = FuelMap.value(fuel.getItem());
        if (burn <= 0) {
            return;
        }
        // 燃烧结果超过上限则不燃烧
        if (heatValue + burn > maxHeatCapacity) {
            return;
        }
        heatValue = MathHelper.clamp(heatValue + burn, 0, maxHeatCapacity);
        ItemStack rem = fuel.getRecipeRemainder();
        if (!rem.isEmpty()) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rem));
        }
        fuel.decrement(1);
        fuelSlotDirty = true;
    }

    /** 水碗/水桶：扣热值并把空容器返还到燃料槽（而不是掉到世界） */
    protected void tryExtinguish(World world, BlockPos pos) {
        int fuelIndex = getFuelSlotIndex();
        ItemStack fuel = inventory.get(fuelIndex);
        int v = FuelMap.value(fuel.getItem()); // 负值
        if (v >= 0) {
            return;
        }
        heatValue = Math.max(0, heatValue + v);
        ItemStack rem = fuel.getRecipeRemainder();
        fuel.decrement(1);
        if (fuel.isEmpty()) {
            // 槽位空了：把空容器放回槽位
            inventory.set(fuelIndex, rem.isEmpty() ? ItemStack.EMPTY : rem.copy());
        } else if (!rem.isEmpty()) {
            // 仍有剩余的水桶/碗：空容器只能掉到世界（槽位已被占用）
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, rem.copy()));
        }
        fuelSlotDirty = true;
    }

    /** 熔化：整座炉子结构（核心 + 外壳 + 特殊方块 + 内部空腔）全部变成岩浆 */
    protected void meltFurnace(World world, BlockPos pos) {
        BlockState masterState = world.getBlockState(pos);
        inventory.replaceAll(ignored -> ItemStack.EMPTY);
        for (BlockPos p : BlastFurnaceStructure.INSTANCE.enumerateBlocks(pos, masterState)) {
            world.setBlockState(p, Blocks.LAVA.getDefaultState());
        }
        fillInteriorLava(world, masterState);
        BlastFurnaceStructure.INSTANCE.invalidate(pos);
    }

    protected boolean hasValidInput() {
        for (int i = 0; i < inputSlotCount; i++) {
            if (parseInput(inventory.get(i)) != null) {
                return true;
            }
        }
        return false;
    }
}
