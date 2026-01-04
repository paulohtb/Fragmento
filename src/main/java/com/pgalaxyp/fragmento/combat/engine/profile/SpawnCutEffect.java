package com.pgalaxyp.fragmento.combat.engine.profile;

import java.util.UUID;

public record SpawnCutEffect(
        UUID ownerId,
        UUID targetId,
        double spawnX,
        double spawnY,
        double spawnZ,
        double aimX,
        double aimY,
        double aimZ,
        int lifeTicks,
        float damage,
        boolean verticalOnly
) implements CombatEffect {}