package com.pgalaxyp.fragmento.features.bard_class.client.render.renderer;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.MelodyZone;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MelodyZoneRenderer extends EntityRenderer<MelodyZone> {

    public MelodyZoneRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(MelodyZone entity) {
        return null;
    }
}