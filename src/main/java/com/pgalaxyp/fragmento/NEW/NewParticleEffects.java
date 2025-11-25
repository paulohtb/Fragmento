package com.pgalaxyp.fragmento.NEW;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

final class NewParticleEffects {

    private static final int FULL_CIRCLE_POINTS = 40;

    private NewParticleEffects() {
    }

    static void spawnSpecialAbilityRingParticles(ServerPlayer player, double radius, double maxRange) {
        if (radius <= 0.0D) {
            return;
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        double y = player.getY();

        int points;
        if (radius < maxRange * 0.4D) {
            points = FULL_CIRCLE_POINTS / 4;
        } else if (radius < maxRange * 0.7D) {
            points = FULL_CIRCLE_POINTS / 2;
        } else {
            points = FULL_CIRCLE_POINTS;
        }

        double step = (2.0D * Math.PI) / points;
        for (int i = 0; i < points; i++) {
            double angle = i * step;
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            double particleX = player.getX() + offsetX;
            double particleZ = player.getZ() + offsetZ;

            level.sendParticles(ParticleTypes.END_ROD, particleX, y, particleZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
}
