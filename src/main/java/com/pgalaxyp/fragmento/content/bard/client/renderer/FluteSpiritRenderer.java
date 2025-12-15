package com.pgalaxyp.fragmento.content.bard.client.renderer;

import com.pgalaxyp.fragmento.content.bard.client.model.FluteSpiritModel;
import com.pgalaxyp.fragmento.content.bard.entity.FluteSkillEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class FluteSpiritRenderer extends GeoEntityRenderer<FluteSkillEntity> {

    public FluteSpiritRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FluteSpiritModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderType getRenderType(
            FluteSkillEntity anim,
            ResourceLocation texture,
            MultiBufferSource bufferSource,
            float partialTick
    ) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public Vec3 getRenderOffset(FluteSkillEntity entity, float partialTick) {
        Vec3 smooth = entity.getSmoothRenderPos(partialTick);

        Vec3 vanilla = new Vec3(
                Mth.lerp(partialTick, entity.xo, entity.getX()),
                Mth.lerp(partialTick, entity.yo, entity.getY()),
                Mth.lerp(partialTick, entity.zo, entity.getZ())
        );

        return smooth.subtract(vanilla);
    }
}
