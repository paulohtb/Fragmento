package com.pgalaxyp.fragmento.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ExposureEffect extends MobEffect {

    public ExposureEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFAA55);

        this.addAttributeModifier(
                Attributes.ARMOR,
                ResourceLocation.fromNamespaceAndPath("fragmento", "armor_reduction"),
                -0.20,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
