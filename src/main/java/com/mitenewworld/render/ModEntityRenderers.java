package com.mitenewworld.render;
import com.mitenewworld.render.UnderWorldPortalCoreRenderer;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.registry.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.util.Identifier;

/**
 * 实体渲染器注册类
 */
public class ModEntityRenderers {
    
    public static final EntityModelLayer SHADOW_LAYER = new EntityModelLayer(
        Identifier.of("mitenewworld", "shadow"), "main"
    );
    public static final EntityModelLayer SKELETON_KNIGHT_LAYER = new EntityModelLayer(
            Identifier.of("mitenewworld", "skeleton_knight"), "main"
    );
    
    public static void registerRenderers() {
        EntityModelLayerRegistry.registerModelLayer(SHADOW_LAYER, () -> {
            // 创建 TexturedModelData
            return TexturedModelData.of(ZombieEntityModel.getModelData(Dilation.NONE ,0.0f), 64, 32);
        });
        EntityModelLayerRegistry.registerModelLayer(SKELETON_KNIGHT_LAYER, () -> {
            // 创建 TexturedModelData
            return TexturedModelData.of(SkeletonEntityModel.getModelData(Dilation.NONE ,0.0f), 64, 32);
        });
        EntityRendererFactories.register(ModEntities.SHADOW_ENTITY, (context) ->
            new ShadowEntityRenderer(context, SHADOW_LAYER));
        EntityRendererFactories.register(ModEntities.SKELETON_KNIGHT_ENTITY, (context) ->
            new SkeletonKnightEntityRenderer(context, SKELETON_KNIGHT_LAYER ));

        EntityRendererFactories.register(ModEntities.UNDER_WORLD_PORTAL_CORE_ENTITY, UnderWorldPortalCoreRenderer::new);

    }
}
