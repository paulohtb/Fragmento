package com.pgalaxyp.fragmento.rpg.action.api;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public record ActionContext(ActorId actorId, WeaponId weaponId, long frameId) {

    public ActionContext {
        Objects.requireNonNull(actorId, "actor id cannot be null");
        Objects.requireNonNull(weaponId, "weapon id cannot be null");
        if (frameId < 0) throw new IllegalArgumentException("frame id must be zero or positive");
    }
}