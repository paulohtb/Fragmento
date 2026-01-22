package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.core.def.SpawnDefaultsProvider;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.flow.*;
import java.util.Objects;

public final class ActorJoinSystem implements FrameSystem {
    private final ActorService actors;
    private final SpawnDefaultsProvider spawnDefaults;

    public ActorJoinSystem(ActorService actors, SpawnDefaultsProvider spawnDefaults) {
        this.actors = Objects.requireNonNull(actors);
        this.spawnDefaults = Objects.requireNonNull(spawnDefaults);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        for (ActorJoinRequested req : bus.events(ActorJoinRequested.class)) {
            var actorId = req.actorId();
            if (state.findActor(actorId).isPresent()) {
                actors.track(actorId);
                continue;
            }
            if (!actors.track(actorId)) continue;

            var d = spawnDefaults.defaultsFor(actorId);
            bus.publish(new ActorSpawned(actorId, d.classId(), d.startingWeaponId(), d.healthHearts(), d.maxHealthHearts()));
            bus.publish(new ActorJoined(actorId));
        }
    }
}