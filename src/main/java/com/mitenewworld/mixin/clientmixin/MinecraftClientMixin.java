package com.mitenewworld.mixin.clientmixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.screen.ModInventoryScreen;
import com.mitenewworld.core.shadow.ShadowMinecraftClient;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.toast.TutorialToast;
import net.minecraft.client.tutorial.TutorialManager;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements ShadowMinecraftClient {

    @Shadow
    private static final Text SOCIAL_INTERACTIONS_NOT_AVAILABLE = null;
    @Shadow
    private int itemUseCooldown;
    @Mutable
    @Final
    @Shadow
    private final NarratorManager narratorManager;
    @Shadow
    private TutorialToast socialInteractionsToast;
    @Final
    @Shadow
    public Mouse mouse;
    @Shadow
    private Overlay overlay;
    @Shadow
    @Nullable
    public Screen currentScreen;
    @Mutable
    @Shadow
    @Final
    public final GameOptions options;
    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Nullable
    @Shadow
    public ClientPlayerInteractionManager interactionManager;
    @Mutable
    @Final
    @Shadow
    @Nullable
    private final TutorialManager tutorialManager;
    @Unique
    public boolean isBreak = false;
    @Mutable
    @Final
    @Shadow
    public final InGameHud inGameHud;
    @Mutable
    @Final
    @Shadow
    public final WorldRenderer worldRenderer;
    @Mutable
    @Final
    @Shadow
    public final GameRenderer gameRenderer;
    @Shadow
    public int attackCooldown;
    @Shadow
    @Nullable
    public HitResult crosshairTarget;
    @Shadow
    @Nullable
    public ClientWorld world;
    @Mutable
    @Final
    @Shadow
    public final ParticleManager particleManager;

    public MinecraftClientMixin(GameOptions options, @Nullable TutorialManager tutorialManager, NarratorManager narratorManager, InGameHud inGameHud, WorldRenderer worldRenderer, GameRenderer gameRenderer1, ParticleManager particleManager) {
        this.options = options;
        this.tutorialManager = tutorialManager;
        this.narratorManager = narratorManager;
        this.inGameHud = inGameHud;
        this.worldRenderer = worldRenderer;
        this.gameRenderer = gameRenderer1;
        this.particleManager = particleManager;
    }

    @Inject(method = "handleInputEvents", at = @At("HEAD"), cancellable = true)
    private void handleInputEvents(CallbackInfo ci) {
        ci.cancel();

        if (this.player != null) {
            this.player.setSneaking(this.options.sneakKey.isPressed());
        }

        // ---------- 视角切换 ----------
        while (this.options.togglePerspectiveKey.wasPressed()) {
            Perspective perspective = this.options.getPerspective();
            this.options.setPerspective(this.options.getPerspective().next());
            if (perspective.isFirstPerson() != this.options.getPerspective().isFirstPerson()) {
                this.gameRenderer.onCameraEntitySet(this.options.getPerspective().isFirstPerson() ? this.getCameraEntity() : null);
            }
            this.isBreak = false;
            this.worldRenderer.scheduleTerrainUpdate();
        }

        // ---------- 平滑摄像机 ----------
        while (this.options.smoothCameraKey.wasPressed()) {
            this.options.smoothCameraEnabled = !this.options.smoothCameraEnabled;
            this.isBreak = false;
        }

        // ---------- 快捷栏 ----------
        for (int i = 0; i < 9; ++i) {
            boolean bl = this.options.saveToolbarActivatorKey.isPressed();
            boolean bl2 = this.options.loadToolbarActivatorKey.isPressed();
            if (!this.options.hotbarKeys[i].wasPressed()) {
                continue;
            }

            if (this.player.isSpectator()) {
                this.inGameHud.getSpectatorHud().selectSlot(i);
                continue;
            }
            if (this.player.isInCreativeMode() && this.currentScreen == null && (bl2 || bl)) {
                CreativeInventoryScreen.onHotbarKeyPress((MinecraftClient)(Object)this, i, bl2, bl);
                continue;
            }
            this.player.getInventory().setSelectedSlot(i);
            this.isBreak = false;
        }

        // ---------- 社交 ----------
        while (this.options.socialInteractionsKey.wasPressed()) {
            if (!this.isConnectedToServer() && !SharedConstants.SOCIAL_INTERACTIONS) {
                this.player.sendMessage(SOCIAL_INTERACTIONS_NOT_AVAILABLE, true);
                this.narratorManager.narrateSystemImmediately(SOCIAL_INTERACTIONS_NOT_AVAILABLE);
                continue;
            }
            if (this.socialInteractionsToast != null) {
                this.socialInteractionsToast.hide();
                this.socialInteractionsToast = null;
            }
            this.setScreen(new SocialInteractionsScreen());
            this.isBreak = false;
        }

        // ---------- 背包 ----------
        while (this.options.inventoryKey.wasPressed()) {
            if (this.interactionManager.hasRidingInventory()) {
                this.player.openRidingInventory();
                continue;
            }
            this.tutorialManager.onInventoryOpened();
            this.setScreen(new ModInventoryScreen(this.player));
            this.isBreak = false;
        }

        // ---------- 进度 ----------
        while (this.options.advancementsKey.wasPressed()) {
            this.setScreen(new AdvancementsScreen(this.player.networkHandler.getAdvancementHandler()));
            this.isBreak = false;
        }

        // ---------- 快捷动作 ----------
        while (this.options.quickActionsKey.wasPressed()) {
            this.getQuickActionsDialog().ifPresent(dialog ->
                    this.player.networkHandler.showDialog((RegistryEntry<Dialog>) dialog, this.currentScreen)
            );
            this.isBreak = false;
        }

        // ---------- 副手 ----------
        while (this.options.swapHandsKey.wasPressed()) {
            if (this.player.isSpectator()) {
                continue;
            }
            this.getNetworkHandler().sendPacket(new PlayerActionC2SPacket( PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN ));
            this.isBreak = false;
        }

        // ---------- 丢弃 ----------
        while (this.options.dropKey.wasPressed()) {
            if (this.player.isSpectator() || !this.player.dropSelectedItem(this.isCtrlPressed())) {
                continue;
            }
            this.player.swingHand(Hand.MAIN_HAND);
            this.isBreak = false;
        }

        // ---------- 聊天 ----------
        while (this.options.chatKey.wasPressed()) {
            this.openChatScreen(ChatHud.ChatMethod.MESSAGE);
            this.isBreak = false;
        }

        if (this.currentScreen == null && this.overlay == null && this.options.commandKey.wasPressed()) {
            this.openChatScreen(ChatHud.ChatMethod.COMMAND);
            this.isBreak = false;
        }

        // ---------- 持续挖掘触发 ----------
        if (this.options.attackKey.isPressed() && this.options.useKey.wasPressed()) {
            this.isBreak = true;
        }

        boolean bl3 = false;

        if (this.player.isUsingItem()) {
            if (!this.options.useKey.isPressed()) {
                this.interactionManager.stopUsingItem(this.player);
            }
            while (this.options.attackKey.wasPressed()) {
                this.isBreak = false;
            }
            while (this.options.useKey.wasPressed()) {
                this.isBreak = false;
            }
            while (this.options.pickItemKey.wasPressed()) {
                this.isBreak = false;
            }
        } else {
            while (this.options.attackKey.wasPressed()) {
                bl3 |= this.doAttack();
                this.isBreak = false;
            }
            while (this.options.useKey.wasPressed()) {
                this.doItemUse();
                this.isBreak = false;
            }
            while (this.options.pickItemKey.wasPressed()) {
                this.doItemPick();
                this.isBreak = false;
            }
            if (this.player.isSpectator()) {
                while (this.options.spectatorHotbarKey.wasPressed()) {
                    this.inGameHud.getSpectatorHud().useSelectedCommand();
                    this.isBreak = false;
                }
            }
        }

        if (this.options.useKey.isPressed() && this.itemUseCooldown == 0 && !this.player.isUsingItem()) {
            this.doItemUse();
        }

        // ---------- 方块破坏（你的核心改动） ----------
        this.handleBlockBreaking(this.isBreak || (this.currentScreen == null && !bl3 && this.options.attackKey.isPressed() && this.mouse.isCursorLocked()));
    }


    @Shadow
    private Optional<Object> getQuickActionsDialog() {
        return Optional.empty();
    }

    @Shadow
    public boolean isCtrlPressed() {
        return false;
    }

    @Shadow
    private void handleBlockBreaking(boolean b) {
    }

    @Shadow
    public Entity getCameraEntity() {
        return null;
    }

    @Nullable
    @Shadow
    public ClientPlayNetworkHandler getNetworkHandler() {
        return this.player == null ? null : this.player.networkHandler;
    }

    @Shadow
    public void setScreen(@Nullable Screen screen) {

    }

    @Shadow
     public void openChatScreen(ChatHud.ChatMethod method) {
    }

    @Shadow
    private boolean doAttack() {
        return false;
    }

    @Shadow
    private void doItemPick() {
    }

    @Shadow
    private void doItemUse() {
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void FixedHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        ci.cancel();
        if (!breaking) {
            this.attackCooldown = 0;
        }
        if (this.attackCooldown > 0 || this.player.isUsingItem()) {
            return;
        }
        if (breaking && this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            Direction direction;
            BlockHitResult blockHitResult = (BlockHitResult)this.crosshairTarget;
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!this.world.getBlockState(blockPos).isAir() && this.interactionManager.updateBlockBreakingProgress(blockPos, direction = blockHitResult.getSide())) {
                Item HandItem = this.player.getMainHandStack().getItem();
                if (HandItem.canMine(player.getMainHandStack() , player.getEntityWorld().getBlockState(blockPos), this.world, blockPos, this.player)) {
                    this.world.spawnBlockBreakingParticle(blockPos, direction);
                }
                this.player.swingHand(Hand.MAIN_HAND);
            }
            return;
        }
        this.interactionManager.cancelBlockBreaking();
    }

    @Shadow
    private boolean isConnectedToServer() {
        return false;
    }


    @Override
    public boolean getBreakState() {
        return isBreak;
    }
}
