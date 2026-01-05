package com.pgalaxyp.fragmento.rpg.gameplay.state;

import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnSide;

public record ComboSnapshot(
        int index,
        boolean executing,
        String activeStepId,
        SpawnSide lastSpawnSide
) {}