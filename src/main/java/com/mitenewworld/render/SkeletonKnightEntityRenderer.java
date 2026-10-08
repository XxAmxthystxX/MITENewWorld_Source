package com.mitenewworld.render;
import com.mitenewworld.entity.mob.ShadowEntity;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.entity.mob.SkeletonKnightEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.SkeletonEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * ShadowEntity 专用渲染器
 * 基于僵尸渲染器，隐藏腿部实现无腿影子的视觉效果
 */
@Environment(EnvType.CLIENT)
public class SkeletonKnightEntityRenderer extends AbstractSkeletonEntityRenderer<SkeletonKnightEntity, SkeletonEntityRenderState> {

    private static final Identifier TEXTURE = Identifier.of("mitenewworld", "textures/entity/mob/skeleton_knight/skeleton_knight.png");
    public SkeletonKnightEntityRenderer(EntityRendererFactory.Context context ,EntityModelLayer skeletonKnightLayer) {
        this(context, EntityModelLayers.SKELETON, EntityModelLayers.SKELETON_EQUIPMENT);
    }
    public SkeletonKnightEntityRenderer(EntityRendererFactory.Context context, EntityModelLayer layer, EquipmentModelData<EntityModelLayer> equipmentModelData) {
        super(context, equipmentModelData, new SkeletonEntityModel<>(context.getPart(layer)));
    }

    @Override
    public SkeletonEntityRenderState createRenderState() {
        return new SkeletonEntityRenderState();
    }

    @Override
    public Identifier getTexture(SkeletonEntityRenderState state) {
        return TEXTURE;
    }



    @Override
    public void render(SkeletonEntityRenderState state, MatrixStack matrices, net.minecraft.client.render.command.OrderedRenderCommandQueue commandQueue, net.minecraft.client.render.state.CameraRenderState cameraState) {
        super.render(state, matrices, commandQueue, cameraState);

    }


    @Override
    protected void setupTransforms(SkeletonEntityRenderState state, MatrixStack matrices, float bodyYaw, float baseHeight) {
        super.setupTransforms(state, matrices, bodyYaw, baseHeight);

    }

    @Override
    protected void scale(SkeletonEntityRenderState state, MatrixStack matrices) {

        super.scale(state, matrices);
        matrices.scale(1.1f, 1.1f, 1.1f);
    }

}
