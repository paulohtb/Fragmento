package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;

import java.util.*;

public interface ActorInputContextProvider {
    Optional<ActorId> localActorId();
    Optional<WeaponId> weaponInHandId(ActorId actorId, InputSnapshotView snapshot);
}