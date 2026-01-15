package com.pgalaxyp.fragmento.combat.input.bridge;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public interface ActorInputContextProvider {
    Optional<ActorId> localActorId();
    Optional<WeaponId> weaponInHandId(ActorId actorId, InputSnapshotView snapshot);
}