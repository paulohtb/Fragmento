package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NewLyreWeapon extends NewAbstractWeapon {

    public NewLyreWeapon(Properties properties) {
        super(properties, NewLyreProjectile::new);
    }

    @Override
    public void useNormalAbility(ServerPlayer player, ItemStack stack) {
        super.useNormalAbility(player, stack);
    }

    private int getHitCount(ItemStack stack) {
        return stack.getOrDefault(NewDataComponents.NORMAL_ABILITY_HIT_COUNT.get(), 0);
    }

    private float getPitchForIndex(int index) {
        return switch (index) {
            case 0 -> 1.2f;
            case 1 -> 1.0f;
            case 2 -> 0.8f;
            default -> 1.0f;
        };
    }

    @Override
    protected void onNormalAbilityFired(ServerPlayer player, ItemStack stack, boolean charged) {

        if (!charged) {
            int hitCount = getHitCount(stack);

            int index = Math.max(0, Math.min(2, hitCount - 1));

            float pitch = getPitchForIndex(index);

            player.level().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.NOTE_BLOCK_GUITAR,
                    player.getSoundSource(),
                    1.0f,
                    pitch
            );
            return;
        }

        float[] sequence = new float[] {
                getPitchForIndex(0),
                getPitchForIndex(1),
                getPitchForIndex(2)
        };

        for (float pitch : sequence) {
            player.level().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.NOTE_BLOCK_GUITAR,
                    player.getSoundSource(),
                    1.0f,
                    pitch
            );
        }
    }

    @Override
    protected void applySpecialAbilityEffectToTarget(ServerPlayer server, ItemStack stack, LivingEntity target) {
        if (target instanceof Player player) {
            player.heal(1F);
            player.addEffect(new MobEffectInstance(EffectsRegistry.HEALING_TOUCH, 20));
        }
    }

    @Override
    protected void onSpecialAbilityTick(ServerPlayer player, ItemStack stack, int ticks) {}
}
