package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import java.util.*;

public final class AbilityResolver {
    private final List<AbilityRule> rules;
    private final Map<WeaponId, AbilityId> primaryByWeapon;
    private final AbilityComboRepository combos = new AbilityComboRepository();

    public AbilityResolver(List<AbilityRule> rules, Map<WeaponId, AbilityId> primaryByWeapon) {
        this.rules = List.copyOf(Objects.requireNonNull(rules));
        this.primaryByWeapon = Map.copyOf(Objects.requireNonNull(primaryByWeapon));
    }

    public AbilityId primaryAbility(WeaponId weaponId) {
        Objects.requireNonNull(weaponId);
        return primaryByWeapon.get(weaponId);
    }

    public AbilityId resolve(ActorId actorId, WeaponId weaponId, AbilityId baseAbility, GameState state, long frameId) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(baseAbility);
        Objects.requireNonNull(state);
        if (frameId < 0) throw new IllegalArgumentException();

        int step = combos.nextStep(actorId, weaponId, frameId);
        for (AbilityRule r : rules) if (r.matches(actorId, weaponId, step, baseAbility, state)) return r.resultAbility();
        return baseAbility;
    }
}