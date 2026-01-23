package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.*;

public final class ComboService {
    private final ComboRepository repo = new ComboRepository();

    public int resolveStep(ActorId actorId, WeaponId weaponId, long frameId) {
        if (actorId == null || weaponId == null) { throw new IllegalArgumentException(); }
        return repo.nextStep(actorId, weaponId, frameId);
    }
}