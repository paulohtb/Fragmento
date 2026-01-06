package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import com.pgalaxyp.fragmento.rpg.core.rule.zone.SpawnRule;

public record ComboStep(
        String stepId,
        SpawnRule spawnRule
) {}