package com.pgalaxyp.fragmento.rpg.core.domain.effect;

import java.util.Map;

public record EffectSpec(
        EffectId id,
        Map<String, String> params
) {}