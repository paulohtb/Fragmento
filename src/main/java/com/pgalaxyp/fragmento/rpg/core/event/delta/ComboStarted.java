package com.pgalaxyp.fragmento.rpg.core.event.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

public record ComboStarted(
        ActorId actorId,
        ActionId actionId,
        WeaponId weaponId,
        int stepsTotal
) implements StateDelta {
    public ComboStarted {
        if (actorId == null || actionId == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        if (stepsTotal <= 0) {
            throw new IllegalArgumentException();
        }
    }
}