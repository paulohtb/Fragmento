package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Optional;

public interface ActorInputContextProvider {
    Optional<ActorId> localActorId();
    Optional<WeaponId> weaponInHandId(ActorId actorId);
}