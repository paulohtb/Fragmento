package com.pgalaxyp.fragmento.rpg.core.events.delta;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ComboStarted(ActorId actorId, ActionKey actionKey, WeaponId weaponId, int stepsTotal, long stepFrameId) implements StateDelta {

    public ComboStarted {
        if (actorId == null || actionKey == null || weaponId == null) {
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