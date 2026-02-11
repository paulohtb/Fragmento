package com.pgalaxyp.fragmento.combat.classModule.api;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.actionModule.api.ActionSlot;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record ClassKit(ClassId id, Set<WeaponId> weapons, Set<AbilityId> abilities, Map<ActionSlot, AbilityId> defaultLoadout) {
    public ClassKit {
        Objects.requireNonNull(id);
        weapons = Set.copyOf(Objects.requireNonNull(weapons));
        abilities = Set.copyOf(Objects.requireNonNull(abilities));
        defaultLoadout = Map.copyOf(Objects.requireNonNull(defaultLoadout));

        if (weapons.isEmpty()) throw new IllegalArgumentException();
        for (var w : weapons) if (w == null) throw new IllegalArgumentException();
        for (var a : abilities) if (a == null) throw new IllegalArgumentException();
        for (var e : defaultLoadout.entrySet()) if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();

        if (!defaultLoadout.isEmpty() && !abilities.containsAll(defaultLoadout.values())) throw new IllegalArgumentException();
    }
}
