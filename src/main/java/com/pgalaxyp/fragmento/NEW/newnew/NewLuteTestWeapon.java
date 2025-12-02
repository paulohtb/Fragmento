package com.pgalaxyp.fragmento.NEW.newnew;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NewLuteTestWeapon extends NewNewAbstractWeapon {

    private static final double RANGE = 8.0;

    public NewLuteTestWeapon(Properties props) {
        super(props);
    }

    @Override
    public void performTargetedAttack(ServerPlayer player) {

        LivingEntity target = RayTraceFinder.findTarget(player, RANGE);
        if (target == null) return;

        Level level = player.level();
        if (level.isClientSide()) return;

        Vec3 targetPos = target.position();

        Vec3 aimBase = new Vec3(
                target.getX(),
                target.getY() + 0.9,
                target.getZ()
        );

        double yVariation = (level.getRandom().nextDouble() * 1.0) - 0.5;
        Vec3 aimPoint = aimBase.add(0, yVariation, 0);

        Vec3 toPlayer = player.position().subtract(targetPos);
        Vec3 horiz = new Vec3(toPlayer.x, 0.0, toPlayer.z);

        if (horiz.lengthSqr() < 1e-4) {
            Vec3 look = player.getLookAngle();
            horiz = new Vec3(look.x, 0.0, look.z);
            if (horiz.lengthSqr() < 1e-4) horiz = new Vec3(0, 0, 1);
        }

        Vec3 dir = horiz.normalize();
        Vec3 right = new Vec3(-dir.z, 0, dir.x);

        double side = ((level.getRandom().nextDouble() * 2.0) - 1.0) * 0.6;
        double dist = 3.0;

        Vec3 spawnPos = aimPoint.add(dir.scale(dist)).add(right.scale(side));

        double groundY = level.getHeight(
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (int) spawnPos.x,
                (int) spawnPos.z
        );

        if (spawnPos.y < groundY + 0.3) {
            spawnPos = new Vec3(spawnPos.x, groundY + 0.3, spawnPos.z);
        }

        NewNewLuteProjectile proj = new NewNewLuteProjectile(level, player);
        proj.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        proj.setTrackedTarget(target);

        Vec3 targetHead = new Vec3(
                target.getX(),
                target.getY() + 0.9,
                target.getZ()
        );

        Vec3 dirToTarget = targetHead.subtract(spawnPos);
        double distanceToHead = dirToTarget.length();

        if (distanceToHead < 1e-4) {
            dirToTarget = new Vec3(0, 1, 0);
            distanceToHead = 1.0;
        }

        Vec3 travelDir = dirToTarget.normalize();

        double desiredTravelDistance = distanceToHead + 1.0;
        double travelTicks = 10.0;
        double speedPerTick = desiredTravelDistance / travelTicks;

        Vec3 velocity = travelDir.scale(speedPerTick);

        proj.setSpawnDelayTicks(10);
        proj.setDeltaMovement(velocity);

        level.addFreshEntity(proj);
    }
}