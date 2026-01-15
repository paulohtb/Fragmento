package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.domain.def.*;
import java.util.*;

public record GameContent(
        NavigableMap<ActionId, ActionDef> actions,
        NavigableMap<WeaponId, WeaponDef> weapons,
        NavigableMap<EffectId, EffectDef> effects
) {
    public GameContent {
        actions = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(actions)));
        weapons = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(weapons)));
        effects = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(effects)));
    }

    public Optional<ActionDef> action(ActionId id) { return Optional.ofNullable(actions.get(id)); }
    public Optional<WeaponDef> weapon(WeaponId id) { return Optional.ofNullable(weapons.get(id)); }
    public Optional<EffectDef> findEffect(EffectId id) { return Optional.ofNullable(effects.get(id)); }
}