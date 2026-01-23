package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.event.*;
import com.pgalaxyp.fragmento.combat.random.*;
import java.util.*;

public final class ActorCommitSystem implements FrameSystem {

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        NavigableMap<ActorId, ActorState> actors = new TreeMap<>(state.actors().actors());
        for (FrameEvent e : bus.events()) if (e != null) applyOne(actors, e);
        bus.view(ActorView.class, new ActorView(actors));
    }

    private static void applyOne(Map<ActorId, ActorState> actors, FrameEvent event) {
        if (event instanceof ActorUpserted(ActorId id, ActorState state)) { actors.put(id, state); return; }
        if (event instanceof ActorRemoved(ActorId id)) { actors.remove(id); }
    }
}