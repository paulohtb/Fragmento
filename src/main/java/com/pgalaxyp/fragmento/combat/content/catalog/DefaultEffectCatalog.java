package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class DefaultEffectCatalog implements EffectCatalog {
    private final NavigableMap<EffectId, EffectDef> byId = new TreeMap<>();

    public void register(EffectDef def) {
        Objects.requireNonNull(def);
        if (byId.putIfAbsent(def.id(), def) != null) throw new IllegalStateException();
    }

    @Override
    public Optional<EffectDef> effect(EffectId effectId) {
        Objects.requireNonNull(effectId);
        return Optional.ofNullable(byId.get(effectId));
    }

    public NavigableMap<EffectId, EffectDef> view() { return Collections.unmodifiableNavigableMap(byId); }
}