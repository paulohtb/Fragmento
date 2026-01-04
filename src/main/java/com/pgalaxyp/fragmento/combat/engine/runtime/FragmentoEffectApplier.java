package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.content.entity.CutEntity;
import com.pgalaxyp.fragmento.combat.content.entity.FragmentoEntities;
import com.pgalaxyp.fragmento.combat.engine.profile.SpawnCutEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class FragmentoEffectApplier {

    public static void applySpawnCut(ServerLevel level, SpawnCutEffect e) {
        if (level == null || e == null) {
            return;
        }

        UUID ownerId = e.ownerId();
        if (ownerId == null) {
            return;
        }

        Vec3 spawn = new Vec3(
                e.spawnX(),
                e.spawnY(),
                e.spawnZ()
        );

        Vec3 targetPoint = new Vec3(
                e.aimX(),
                e.aimY(),
                e.aimZ()
        );

        CutEntity.spawn(
                level,
                FragmentoEntities.CUT.get(),
                ownerId,
                e.targetId(),
                targetPoint,
                spawn,
                e.lifeTicks(),
                e.damage()
        );
    }

    private FragmentoEffectApplier() {}
}