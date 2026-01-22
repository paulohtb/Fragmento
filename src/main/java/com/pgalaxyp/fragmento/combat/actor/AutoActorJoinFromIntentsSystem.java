package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class AutoActorJoinFromIntentsSystem implements FrameSystem {
    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        Set<ActorId> requested = null;

        for (IntentEnvelope env : bus.intents(IntentEnvelope.class)) {
            ActorId actorId = env.actorId();
            if (state.findActor(actorId).isPresent()) continue;

            if (requested == null) requested = new HashSet<>();
            if (!requested.add(actorId)) continue;

            bus.publish(new ActorJoinRequested(actorId));
        }
    }
}