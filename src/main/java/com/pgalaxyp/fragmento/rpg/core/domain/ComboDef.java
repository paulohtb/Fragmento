package com.pgalaxyp.fragmento.rpg.core.domain;

import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;

public record ComboDef(
        int stepsTotal,
        List<EffectId> effectsPerStep
) {}