package com.pgalaxyp.fragmento.feature.bard_class.common.spirit;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SpiritSpawner {

    private static final Logger LOGGER = LogUtils.getLogger();

    private SpiritSpawner() {
    }

    public static <S extends SpiritTargetBase> void configureAndSpawn(
            S spirit,
            LivingEntity caster,
            LivingEntity target,
            ServerLevel level,
            double minForward,
            double maxForward,
            double maxSideOffset,
            int idleTicks,
            int travelTicks,
            double collisionRadius,
            int extraLifetimeTicks
    ) {
        LOGGER.info("=== SPAWNING SPIRIT ===");
        LOGGER.info("Caster={} Target={}", caster.getName().getString(), target.getName().getString());

        Vec3 spawnPos = SpiritSpawnUtil.area3x3Front(caster, level);
        LOGGER.info("SpawnPos={} {} {}", spawnPos.x, spawnPos.y, spawnPos.z);

        spirit.setOwner(caster);
        spirit.setTarget(target);
        spirit.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        Vec3 tp = spirit.getTargetPosition();
        LOGGER.info("TargetPos={} {} {}", tp.x, tp.y, tp.z);

        spirit.faceInstantlyTowards(tp);

        spirit.setSpawnDelay(idleTicks);
        LOGGER.info("spawnDelay={} ticks", idleTicks);

        double dist = spawnPos.distanceTo(tp);
        LOGGER.info("Travel distance={}", dist);

        if (spirit instanceof BardSpirit bs) {
            bs.selectTravelAnimation(dist);
        }

        int travel = selectTravelTicks(dist);
        LOGGER.info("Calculated travelTicks={}", travel);

        spirit.configureFlight(idleTicks, travel, collisionRadius);

        int despawnTicks;
        if (spirit instanceof BardSpirit bsCharged && bsCharged.isCharged()) {
            despawnTicks = 30;
        } else {
            despawnTicks = 5;
        }
        spirit.setDespawnDurationTicks(despawnTicks);

        int maxLifetime = idleTicks + travel + extraLifetimeTicks + despawnTicks + 10;
        spirit.setMaxLifetime(maxLifetime);

        LOGGER.info("despawnDuration={} ticks", despawnTicks);
        LOGGER.info("maxLifetime={} ticks", maxLifetime);

        level.addFreshEntity(spirit);
        LOGGER.info("=== SPIRIT ADDED TO WORLD id={} ===", spirit.getId());
    }

    private static int selectTravelTicks(double dist) {
        if (dist <= 4.0) {
            return ticks(0.32);
        }
        if (dist <= 8.0) {
            return ticks(0.48);
        }
        return ticks(0.72);
    }

    private static int ticks(double seconds) {
        return (int)(seconds * 20.0);
    }
}
