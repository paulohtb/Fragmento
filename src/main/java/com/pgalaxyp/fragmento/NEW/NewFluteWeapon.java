package com.pgalaxyp.fragmento.NEW;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NewFluteWeapon extends NewAbstractWeapon {

    public NewFluteWeapon(Properties properties) {
        super(properties, NewFluteProjectile::new);
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
                    SoundEvents.NOTE_BLOCK_FLUTE,
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
                    SoundEvents.NOTE_BLOCK_FLUTE,
                    player.getSoundSource(),
                    1.0f,
                    pitch
            );
        }
    }

    @Override
    protected void applySpecialAbilityEffectToTarget(ServerPlayer server, ItemStack stack, LivingEntity target) {
        if (target instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20));
        } else if (target instanceof Mob) {
            double radius = 6.0D;

            Vec3 playerPos = server.position();
            List<Mob> mobs = server.level().getEntitiesOfClass(
                    Mob.class,
                    server.getBoundingBox().inflate(radius)
            );

            for (Mob mob : mobs) {

                if (!mob.isAlive() || !mob.isPushable()) continue;

                Vec3 diff = mob.position().subtract(playerPos);
                Vec3 horizontal = new Vec3(diff.x, 0.0D, diff.z);
                if (horizontal.lengthSqr() < 1.0E-4D) continue;
                Vec3 dir = horizontal.normalize();
                Vec3 push = dir.scale(1.75D);
                Vec3 current = mob.getDeltaMovement();

                mob.setDeltaMovement(current.add(push.x, 0.5D, push.z));
                mob.hurtMarked = true;
            }
        }
    }

    @Override
    protected void onSpecialAbilityTick(ServerPlayer player, ItemStack stack, int ticks) {}
}
