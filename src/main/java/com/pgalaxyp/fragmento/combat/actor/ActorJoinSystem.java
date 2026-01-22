package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.core.def.SpawnDefaultsResolver;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import java.util.Objects;

public final class ActorJoinSystem implements FrameSystem {
    private final ActorService actors;
    private final SpawnDefaultsResolver spawnDefaults;

    public ActorJoinSystem(ActorService actors, SpawnDefaultsResolver spawnDefaults) {
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

            var d = spawnDefaults.resolve(actorId).orElseThrow(() -> new IllegalStateException("No spawn defaults for actorId=" + actorId.uuid()));
            bus.publish(new ActorSpawned(actorId, d.classId(), d.startingWeaponId(), d.healthHearts(), d.maxHealthHearts()));
        }
    }
}