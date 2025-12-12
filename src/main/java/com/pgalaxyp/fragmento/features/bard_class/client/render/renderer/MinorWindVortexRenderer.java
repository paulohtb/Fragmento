package com.pgalaxyp.fragmento.features.bard_class.client.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.features.bard_class.client.render.model.MinorWindVortexGeoModel;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.MinorWindVortex;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MinorWindVortexRenderer extends GeoEntityRenderer<MinorWindVortex> {

    public MinorWindVortexRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MinorWindVortexGeoModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(MinorWindVortex anim, ResourceLocation tex, MultiBufferSource buf, float pt) {
        return RenderType.entityTranslucent(tex);
    }

    @Override
    public void preRender(
            PoseStack stack,
            MinorWindVortex anim,
            BakedGeoModel model,
            MultiBufferSource buf,
            VertexConsumer vc,
            boolean rerender,
            float pt,
            int light,
            int overlay,
            int color
    ) {
        stack.scale(4.0f, 4.0f, 4.0f);
        super.preRender(stack, anim, model, buf, vc, rerender, pt, light, overlay, color);
    }
}