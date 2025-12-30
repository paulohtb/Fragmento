package com.pgalaxyp.fragmento.combat.old.client.render.entity;

import com.pgalaxyp.fragmento.combat.old.client.render.model.FluteSpiritModel;
import com.pgalaxyp.fragmento.combat.old.system.entity.host.BardSpiritEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class FluteSpiritRenderer extends GeoEntityRenderer<BardSpiritEntity> {

    public FluteSpiritRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FluteSpiritModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(
            BardSpiritEntity anim,
            ResourceLocation texture,
            MultiBufferSource bufferSource,
            float partialTick
    ) {
        return RenderType.entityTranslucent(texture);
    }
}