package com.pgalaxyp.fragmento.rpg.effect.gameplay;

import com.pgalaxyp.fragmento.rpg.content.entity.CutOrientation;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;

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
        CutOrientation orientation
) implements RpgEffect {}