package com.pgalaxyp.fragmento.combat.action.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public interface ActionService {
    ActionOutcome handle(ActorId actorId, WeaponId weaponId, long frameId, ActionRequest request);

    boolean hasActive(ActorId actorId);
    void clear(ActorId actorId);
    void clearAll();
}