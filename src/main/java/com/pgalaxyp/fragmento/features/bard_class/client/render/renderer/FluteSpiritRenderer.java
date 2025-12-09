package com.pgalaxyp.fragmento.features.bard_class.client.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.features.bard_class.client.render.model.FluteSpiritGeoModel;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.FluteSpirit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FluteSpiritRenderer extends GeoEntityRenderer<FluteSpirit> {

    public FluteSpiritRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FluteSpiritGeoModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(FluteSpirit anim, ResourceLocation tex, MultiBufferSource buf, float pt) {
        return RenderType.entityTranslucent(tex);
    }

    @Override
    public void preRender(
            PoseStack stack,
            FluteSpirit anim,
            BakedGeoModel model,
            MultiBufferSource buf,
            VertexConsumer vc,
            boolean rerender,
            float pt,
            int light,
            int overlay,
            int color
    ) {
        stack.mulPose(Axis.YP.rotationDegrees(anim.getYRot()));
        stack.mulPose(Axis.XP.rotationDegrees(anim.getXRot()));

        super.preRender(stack, anim, model, buf, vc, rerender, pt, light, overlay, color);
    }
}
