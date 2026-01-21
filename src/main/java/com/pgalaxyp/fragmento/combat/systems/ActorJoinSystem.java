package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.actor.ActorService;
import com.pgalaxyp.fragmento.combat.core.def.SpawnDefaultsProvider;
import com.pgalaxyp.fragmento.combat.delta.StateDelta;
import com.pgalaxyp.fragmento.combat.events.actor.ActorJoinRequested;
import com.pgalaxyp.fragmento.combat.events.actor.ActorJoined;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import java.util.List;
import java.util.Objects;

public final class ActorJoinSystem implements FrameSystem {
    private final ActorService actors;
    private final SpawnDefaultsProvider defaults;

    public ActorJoinSystem(ActorService actors, SpawnDefaultsProvider defaults) {
        this.actors = Objects.requireNonNull(actors);
        this.defaults = Objects.requireNonNull(defaults);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (ActorJoinRequested req : bus.events(ActorJoinRequested.class)) {
            List<StateDelta> deltas = actors.onJoin(req.actorId(), state, defaults.defaultsFor(req.actorId()));
            for (StateDelta d : deltas) bus.emit(d);
            bus.publish(new ActorJoined(req.actorId()));
        }
    }
}