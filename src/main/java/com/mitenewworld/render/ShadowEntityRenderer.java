package com.mitenewworld.render;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.entity.mob.ShadowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.render.entity.state.ZombieEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ShadowEntityRenderer extends LivingEntityRenderer<ShadowEntity, ZombieEntityRenderState, ZombieEntityModel<ZombieEntityRenderState>> {

    private static final Identifier TEXTURE = Identifier.of("mitenewworld", "textures/entity/mob/shadow/shadow.png");



    public ShadowEntityRenderer(EntityRendererFactory.Context context, EntityModelLayer layer) {
        super(context, new ZombieEntityModel<>(context.getPart(layer)), 0.5F);
    }

    @Override
    public ZombieEntityRenderState createRenderState() {
        return new ZombieEntityRenderState();
    }

    @Override
    public Identifier getTexture(ZombieEntityRenderState state) {
        return TEXTURE;
    }


    @Override
    public void render(ZombieEntityRenderState state, MatrixStack matrices, net.minecraft.client.render.command.OrderedRenderCommandQueue commandQueue, net.minecraft.client.render.state.CameraRenderState cameraState) {

        this.model.leftLeg.visible = false;
        this.model.rightLeg.visible = false;

        super.render(state, matrices, commandQueue, cameraState);

    }


        @Override
        protected void setupTransforms(ZombieEntityRenderState state, MatrixStack matrices, float bodyYaw, float baseHeight) {
            super.setupTransforms(state, matrices, bodyYaw, baseHeight);

            // 漂浮效果
            float floatHeight = MathHelper.sin(state.age * 0.1f) * 0.1f;
            matrices.translate(0, floatHeight, 0);

            // 隐藏腿部
            this.model.leftLeg.visible = false;
            this.model.rightLeg.visible = false;
        }

        @Override
        protected void scale(ZombieEntityRenderState state, MatrixStack matrices) {
            super.scale(state, matrices);
        }

}
