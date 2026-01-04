package com.pgalaxyp.fragmento.rpg.effect.runtime;

import com.pgalaxyp.fragmento.rpg.content.entity.CutEffectEntity;
import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg.effect.gameplay.SpawnCutEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public final class EffectApplier {

    public static void applySpawnCut(ServerLevel level, SpawnCutEffect e) {
        if (level == null || e == null) return;

        Vec3 spawn = new Vec3(e.spawnX(), e.spawnY(), e.spawnZ());
        Vec3 target = new Vec3(e.aimX(), e.aimY(), e.aimZ());

        CutEffectEntity.spawn(
                level,
                RpgEntityRegistry.CUT.get(),
                e.ownerId(),
                e.targetId(),
                target,
                spawn,
                e.lifeTicks(),
                e.damage(),
                e.orientation()
        );
    }

    private EffectApplier() {}
}