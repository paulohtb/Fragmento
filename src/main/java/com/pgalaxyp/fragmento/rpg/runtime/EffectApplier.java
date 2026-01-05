package com.pgalaxyp.fragmento.rpg.runtime;

import com.pgalaxyp.fragmento.rpg.content.entity.CutEffectEntity;
import com.pgalaxyp.fragmento.rpg.content.entity.InfusedStrikeEntity;
import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg.effect.gameplay.SpawnCutEffect;
import com.pgalaxyp.fragmento.rpg.effect.gameplay.SpawnInfusedStrikeEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class EffectApplier {

    public static void applyCut(ServerPlayer player, SpawnCutEffect e) {
        if (player == null || e == null) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        CutEffectEntity.spawn(
                sl,
                RpgEntityRegistry.CUT.get(),
                e.ownerId(),
                e.targetId(),
                new Vec3(e.spawnX(), e.spawnY(), e.spawnZ()),
                new Vec3(e.targetX(), e.targetY(), e.targetZ()),
                e.lifeTicks(),
                e.damage(),
                e.orientation()
        );
    }

    public static void applySpawnInfusedStrike(ServerPlayer player, SpawnInfusedStrikeEffect e) {
        if (player == null || e == null) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        InfusedStrikeEntity.spawn(
                sl,
                RpgEntityRegistry.INFUSED_STRIKE.get(),
                e.ownerId(),
                e.damage(),
                new Vec3(e.startX(), e.startY(), e.startZ()),
                new Vec3(e.impactX(), e.impactY(), e.impactZ())
        );
    }

    private EffectApplier() {}
}