package com.pgalaxyp.fragmento.combat.action.runtime;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.action.api.*;

public interface ActionRuntime {
    ActionOutcome handle(ActorId actorId, WeaponId weaponId, long frameId, ActionRequest request);
}