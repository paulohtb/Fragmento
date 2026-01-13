package com.pgalaxyp.fragmento.rpg.input.bridge;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public interface ActorInputContextProvider {
    Optional<ActorId> localActorId();
    Optional<WeaponId> weaponInHandId(ActorId actorId, InputSnapshotView snapshot);
}