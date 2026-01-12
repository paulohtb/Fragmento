package com.pgalaxyp.fragmento.rpg.input.bridge;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import java.util.Optional;

public interface ActorContextProvider {
    Optional<ActorId> localActorId();
    Optional<WeaponId> weaponInHandId(ActorId actorId, SnapshotView snapshot);
}