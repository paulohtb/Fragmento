package com.pgalaxyp.fragmento.combat.effectModule.system;

import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import java.util.Set;

public final class EffectModule {
    public static EffectService create(Set<EffectId> knownEffects) {
        return new KnownEffectService(knownEffects);
    }

    private EffectModule() {}
}