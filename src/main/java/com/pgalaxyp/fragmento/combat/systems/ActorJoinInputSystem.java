package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.events.actor.ActorJoinRequested;
import com.pgalaxyp.fragmento.combat.flow.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.core.state.GameState;

public final class ActorJoinInputSystem implements FrameSystem {
    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (IntentEnvelope envelope : bus.intents(IntentEnvelope.class)) {
            if (envelope.intent() instanceof ActorJoinIntent) {
                bus.publish(new ActorJoinRequested(envelope.actorId()));
            }
        }
    }
}