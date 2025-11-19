package com.pgalaxyp.fragmento.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class ConcussionEffect extends MobEffect {

    private static final Random RANDOM = new Random();
    private static final ResourceLocation SPEED_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("fragmento", "stun_speed_modifier");

    public ConcussionEffect() {
        super(MobEffectCategory.HARMFUL, 0xAACCCC);

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_ID,
                -0.50D,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Mob mob) {

            if (RANDOM.nextFloat() < 0.45F) {
                float randomYaw = mob.getYRot() + (RANDOM.nextFloat() - 0.5F) * 45.0F;
                mob.setYRot(randomYaw);
                mob.yHeadRot = mob.getYRot();
                mob.yBodyRot = mob.getYRot();

                PathNavigation nav = mob.getNavigation();
                if (nav != null && RANDOM.nextFloat() < 0.4F) {
                    nav.stop();
                }

                GoalSelector goals = mob.goalSelector;
                if (goals != null && RANDOM.nextFloat() < 0.3F) {
                    mob.setAggressive(false);
                    mob.setTarget(null);
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}