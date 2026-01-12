package com.pgalaxyp.fragmento.rpg.core.events.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

public record ComboStarted(
        ActorId actorId,
        ActionId actionId,
        WeaponId weaponId,
        int stepsTotal,
        long stepFrameId
) implements StateDelta {
    public ComboStarted {
        if (actorId == null || actionId == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        if (stepsTotal <= 0) {
            throw new IllegalArgumentException();
        }
        if (stepFrameId < 0) {
            throw new IllegalArgumentException();
        }
    }
}