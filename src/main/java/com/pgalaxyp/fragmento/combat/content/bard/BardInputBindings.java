package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import java.util.Map;

public final class BardInputBindings {
    public static Map<WeaponId, AbilityId> primaryAbilityByWeapon() {
        return Map.of(BardIds.FLUTE, BardIds.FLUTE_NOTE);
    }

    private BardInputBindings() {}
}