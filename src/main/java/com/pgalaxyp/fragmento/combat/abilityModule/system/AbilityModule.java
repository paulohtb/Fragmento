package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import java.util.Map;

public final class AbilityModule {
    public static AbilitySystems create(Map<AbilityId, AbilityDefinition> defs) {
        var engine = new AbilityEngine(defs);
        return new AbilitySystems(new AbilityCommandSystem(engine), new AbilityRuntimeSystem(engine));
    }

    private AbilityModule() {}
}