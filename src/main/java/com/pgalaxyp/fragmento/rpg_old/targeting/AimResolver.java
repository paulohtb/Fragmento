package com.pgalaxyp.fragmento.rpg_old.targeting;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ProjectileUtil;

public final class AimResolver {

    public record Aim(
            LivingEntity target,
            Vec3 point
    ) {}

    public Aim resolve(ServerPlayer player, double maxRange, double fallbackDistance) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = eye.add(look.scale(maxRange));

        Level level = player.level();

        EntityHitResult hit = ProjectileUtil.getEntityHitResult(
                level,
                player,
                eye,
                end,
                player.getBoundingBox().expandTowards(look.scale(maxRange)),
                e -> e instanceof LivingEntity le && le.isAlive() && e != player
        );

        if (hit != null && hit.getEntity() instanceof LivingEntity le) {
            return new Aim(le, hit.getLocation());
        }

        return new Aim(null, eye.add(look.scale(fallbackDistance)));
    }
}