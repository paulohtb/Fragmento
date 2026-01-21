package com.pgalaxyp.fragmento.combat.intent;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import java.util.Objects;

public record AbilityUseIntent(AbilityId abilityId, WeaponId weaponId) implements DomainIntent {
    public AbilityUseIntent {
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(weaponId);
    }
}