package com.pgalaxyp.fragmento.entity.bard.angel.aeolus_angel;

import com.pgalaxyp.fragmento.NEW.EntitiesRegistry;
import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
import net.minecraft.world.entity.player.Player;
import com.pgalaxyp.fragmento.registry.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.effect.*;
import java.util.List;

public class AeolusAngel extends AbstractAngel {

    private static final int ORBIT_DURATION_TICKS = 50;
    private static final double ORBIT_ANGULAR_SPEED = 0.25D;
    private static final double MAX_HEIGHT_OFFSET = 4.0D;

    private static final String TAG_ORBIT_TIMES = "orbit_times";
    private static final String TAG_ORBIT_BASE_ANGLE = "orbit_base_angle";
    private static final String TAG_THROW_COOLDOWN = "aeolus_throw_cooldown";

    public AeolusAngel(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    public AeolusAngel(Level level, LivingEntity owner) {
        super(EntitiesRegistry.AEOLUS_ANGEL.get(), level, owner);
    }

    @Override
    protected MobEffectInstance createDurationEffect(int durationTicks) {
        return new MobEffectInstance(
                EffectsRegistry.AEOLUS_BLESSING,
                durationTicks,
                0,
                false,
                false,
                true
        );
    }

    @Override
    protected void applyBuffsToPlayers(List<Player> players) {
        for (Player player : players) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED, 10, 1, false, false, false));
            player.addEffect(new MobEffectInstance(
                    MobEffects.DIG_SPEED, 10, 2, false, false, false));
        }
    }

    @Override
    protected void applyDebuffsToEntities(List<LivingEntity> entities) {
        if (this.owner == null || !this.owner.isAlive()) {
            return;
        }

        CompoundTag persistentData = this.getPersistentData();
        if (!persistentData.contains(TAG_ORBIT_TIMES)) {
            persistentData.put(TAG_ORBIT_TIMES, new CompoundTag());
        }
        CompoundTag orbitTimes = persistentData.getCompound(TAG_ORBIT_TIMES);

        for (LivingEntity entity : entities) {
            if (entity == this || entity == this.owner) {
                continue;
            }

            if (!isLightweight(entity)) {
                continue;
            }

            if (entity.getPersistentData().contains(TAG_THROW_COOLDOWN)) {
                int cooldown = entity.getPersistentData().getInt(TAG_THROW_COOLDOWN);
                if (cooldown > 0) {
                    entity.getPersistentData().putInt(TAG_THROW_COOLDOWN, cooldown - 1);
                    continue;
                } else {
                    entity.getPersistentData().remove(TAG_THROW_COOLDOWN);
                }
            }

            if (!entity.getPersistentData().contains(TAG_ORBIT_BASE_ANGLE)) {
                double baseAngle = Math.atan2(
                        entity.getZ() - this.owner.getZ(),
                        entity.getX() - this.owner.getX()
                );
                entity.getPersistentData().putDouble(TAG_ORBIT_BASE_ANGLE, baseAngle);
            }

            double baseAngle = entity.getPersistentData().getDouble(TAG_ORBIT_BASE_ANGLE);

            String id = entity.getUUID().toString();
            int t = orbitTimes.contains(id) ? orbitTimes.getInt(id) : 0;
            orbitTimes.putInt(id, t + 1);

            if (t >= ORBIT_DURATION_TICKS) {
                orbitTimes.remove(id);
                entity.getPersistentData().remove(TAG_ORBIT_BASE_ANGLE);

                Vec3 inertia = entity.getDeltaMovement();
                if (inertia.lengthSqr() < 0.0001D) {
                    inertia = new Vec3(1.0D, 0.0D, 0.0D);
                }

                Vec3 launch = inertia.normalize();
                launch = new Vec3(launch.x * 2.5D, inertia.y * 3.0D, launch.z * 2.5D);

                entity.setDeltaMovement(launch);
                entity.hurtMarked = true;
                entity.getPersistentData().putInt(TAG_THROW_COOLDOWN, 5);
                continue;
            }

            double progress = (double) t / ORBIT_DURATION_TICKS;
            double currentHeight = MAX_HEIGHT_OFFSET * progress;

            double centerX = this.owner.getX();
            double centerY = this.owner.getY() + currentHeight;
            double centerZ = this.owner.getZ();

            double angle = baseAngle + t * ORBIT_ANGULAR_SPEED;

            double targetX = centerX + Math.cos(angle) * DEBUFF_RADIUS;
            double targetZ = centerZ + Math.sin(angle) * DEBUFF_RADIUS;
            double targetY = centerY;

            Vec3 move = new Vec3(
                    (targetX - entity.getX()) * 0.25D,
                    (targetY - entity.getY()) * 0.20D,
                    (targetZ - entity.getZ()) * 0.25D
            );

            if (move.y < 0.0D) {
                move = new Vec3(move.x, 0.0D, move.z);
            }

            entity.setDeltaMovement(move);
            entity.hurtMarked = true;
        }

        persistentData.put(TAG_ORBIT_TIMES, orbitTimes);
    }

    private boolean isLightweight(LivingEntity entity) {
        EntityType<?> type = entity.getType();

        if (type == EntityType.IRON_GOLEM
                || type == EntityType.RAVAGER
                || type == EntityType.WARDEN
                || type == EntityType.ENDER_DRAGON
                || type == EntityType.WITHER) {
            return false;
        }

        float width = entity.getBbWidth();
        float height = entity.getBbHeight();

        if (width > 1.5F || height > 3.0F) {
            return false;
        }

        if (type.toString().toLowerCase().contains("slime")) {
            return width <= 1.0F;
        }

        return true;
    }
}