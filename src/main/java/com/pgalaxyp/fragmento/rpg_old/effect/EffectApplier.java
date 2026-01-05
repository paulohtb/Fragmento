package com.pgalaxyp.fragmento.rpg_old.effect;

import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnCutEffect;
import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnInfusedStrikeEffect;
import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnSpeedZoneEffect;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class EffectApplier {

    public static UUID applyCut(ServerPlayer player, SpawnCutEffect e) {
        return e.spawn(player);
    }

    public static UUID applySpawnInfusedStrike(ServerPlayer player, SpawnInfusedStrikeEffect e) {
        return e.spawn(player);
    }

    public static UUID applySpawnSpeedZone(ServerPlayer player, SpawnSpeedZoneEffect e) {
        return e.spawn(player);
    }

    private EffectApplier() {}
}