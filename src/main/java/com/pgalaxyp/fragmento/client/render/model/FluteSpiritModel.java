package com.pgalaxyp.fragmento.client.render.model;

import com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public final class FluteSpiritModel extends GeoModel<BardSpiritEntity> {

    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath("fragmento", "geo/entity/flute_spirit.geo.json");

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/flute_spirit.png");

    private static final ResourceLocation ANIMATION =
            ResourceLocation.fromNamespaceAndPath("fragmento", "animations/entity/flute_spirit.animation.json");

    @Override
    public ResourceLocation getModelResource(BardSpiritEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(BardSpiritEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(BardSpiritEntity animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(BardSpiritEntity entity, long instanceId, AnimationState<BardSpiritEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        GeoBone root = getAnimationProcessor().getBone("root");
        if (root == null) return;

        float yaw = Mth.wrapDegrees(entity.getYRot());
        float pitch = Mth.wrapDegrees(entity.getXRot());

        root.setRotY((float) Math.toRadians(-yaw));
        root.setRotX((float) Math.toRadians(-pitch));
    }
}