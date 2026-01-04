package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.content.entity.CutEntity;
import com.pgalaxyp.fragmento.combat.content.entity.FragmentoEntities;
import com.pgalaxyp.fragmento.combat.engine.profile.SpawnCutEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

        Entity ownerEntity = level.getEntity(ownerId);
        if (!(ownerEntity instanceof LivingEntity owner)) {
            return;
        }

        LivingEntity target = null;
        UUID targetId = e.targetId();
        if (targetId != null) {
            Entity targetEntity = level.getEntity(targetId);
            if (targetEntity instanceof LivingEntity le && le.isAlive()) {
                target = le;
            }
        }

        Vec3 spawn = new Vec3(e.spawnX(), e.spawnY(), e.spawnZ());
        Vec3 aim = new Vec3(e.aimX(), e.aimY(), e.aimZ());

        CutEntity.spawn(
                level,
                FragmentoEntities.CUT.get(),
                owner,
                target,
                spawn,
                aim,
                e.lifeTicks(),
                e.damage(),
                e.verticalOnly()
        );
    }

    private FragmentoEffectApplier() {}
}