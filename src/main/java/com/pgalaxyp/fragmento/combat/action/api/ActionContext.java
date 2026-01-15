package com.pgalaxyp.fragmento.combat.action.api;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public record ActionContext(ActorId actorId, WeaponId weaponId, long frameId) {

    public ActionContext {
        Objects.requireNonNull(actorId, "actor id cannot be null");
        Objects.requireNonNull(weaponId, "weapon id cannot be null");
        if (frameId < 0) throw new IllegalArgumentException("frame id must be zero or positive");
    }
}