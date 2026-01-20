package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import java.util.*;

public record AbilityDef(
        AbilityId id,
        int durationFrames,
        int cooldownFrames,
        EffectId startEffect,
        TargetingSpec targeting
) {
    public AbilityDef {
        Objects.requireNonNull(id);
        Objects.requireNonNull(startEffect);
        Objects.requireNonNull(targeting);
        if (durationFrames <= 0) throw new IllegalArgumentException();
        if (cooldownFrames < 0) throw new IllegalArgumentException();
    }
}