package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.*;

public final class DefaultActorService implements ActorService {

    @Override
    public List<StateDelta> onJoin(ActorId actorId, GameState state, SpawnDefaults defaults) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(state);
        Objects.requireNonNull(defaults);

        if (state.findActor(actorId).isPresent()) return List.of();

        return List.of(new ActorSpawned(
                actorId,
                defaults.classId(),
                defaults.startingWeaponId(),
                defaults.healthHearts(),
                defaults.maxHealthHearts()
        ));
    }
}
