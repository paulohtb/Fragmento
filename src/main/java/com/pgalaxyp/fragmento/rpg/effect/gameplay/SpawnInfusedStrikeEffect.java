package com.pgalaxyp.fragmento.rpg.effect.gameplay;

import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;

import java.util.UUID;

public record SpawnInfusedStrikeEffect(
        UUID ownerId,
        float damage,
        double startX,
        double startY,
        double startZ,
        double impactX,
        double impactY,
        double impactZ
) implements RpgEffect {}