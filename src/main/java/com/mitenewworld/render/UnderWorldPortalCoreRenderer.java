package com.mitenewworld.render;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.entity.blockentity.portal.UnderWorldPortalCoreEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class UnderWorldPortalCoreRenderer extends EntityRenderer<UnderWorldPortalCoreEntity , UnderWorldPortalCoreRenderer.UnderWorldPortalCoreEntityRenderState> {

    public UnderWorldPortalCoreRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public UnderWorldPortalCoreEntityRenderState createRenderState() {
        return new UnderWorldPortalCoreEntityRenderState();
    }

    public static class UnderWorldPortalCoreEntityRenderState extends EntityRenderState {

    }
}
