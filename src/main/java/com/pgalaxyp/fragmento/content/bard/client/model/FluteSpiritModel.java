package com.pgalaxyp.fragmento.content.bard.client.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import com.pgalaxyp.fragmento.content.bard.entity.FluteSkillEntity;

public final class FluteSpiritModel extends GeoModel<FluteSkillEntity> {

    private static final ResourceLocation MODEL =
            ResourceLocation.fromNamespaceAndPath("fragmento", "geo/entity/flute_spirit.geo.json");

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/entity/flute_spirit.png");

    private static final ResourceLocation ANIMATION =
            ResourceLocation.fromNamespaceAndPath("fragmento", "animations/entity/flute_spirit.animation.json");

    @Override
    public ResourceLocation getModelResource(FluteSkillEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FluteSkillEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FluteSkillEntity animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(
            FluteSkillEntity entity,
            long instanceId,
            AnimationState<FluteSkillEntity> animationState
    ) {
        super.setCustomAnimations(entity, instanceId, animationState);

        GeoBone root = getAnimationProcessor().getBone("root");
        if (root == null) return;

        float yaw = Mth.wrapDegrees(entity.getYRot());
        float pitch = Mth.wrapDegrees(entity.getXRot());

        root.setRotY((float) Math.toRadians(-yaw));
        root.setRotX((float) Math.toRadians(-pitch));
    }
}
