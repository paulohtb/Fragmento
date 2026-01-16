package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.core.ids.EffectId;
import com.pgalaxyp.fragmento.combat.effect.model.EffectDef;
import java.util.*;

public final class EffectCatalog {

    private final Map<EffectId, EffectDef> defs;

    public EffectCatalog(Map<EffectId, EffectDef> defs) {
        this.defs = Map.copyOf(defs);
    }

    public Optional<EffectDef> effect(EffectId id) {
        return Optional.ofNullable(defs.get(id));
    }
}