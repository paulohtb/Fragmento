package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.actor.api.ActorService;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.events.actor.ActorJoined;
import com.pgalaxyp.fragmento.combat.events.actor.ActorJoinRequested;
import com.pgalaxyp.fragmento.combat.flow.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import java.util.*;

public final class ActorJoinSystem implements FrameSystem {
    private final ActorService actors;
    private final GameContent content;

    public ActorJoinSystem(ActorService actors, GameContent content) {
        this.actors = Objects.requireNonNull(actors);
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (IntentEnvelope env : bus.intents(IntentEnvelope.class)) {
            if (!(env.intent() instanceof ActorJoinIntent)) continue;
            bus.publish(new ActorJoinRequested(env.actorId()));
            for (var d : actors.onJoin(env.actorId(), state, content.defaults())) bus.emit(d);
            bus.publish(new ActorJoined(env.actorId()));
        }
    }
}
