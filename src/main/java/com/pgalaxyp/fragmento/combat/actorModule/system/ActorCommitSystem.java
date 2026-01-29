package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.event.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import java.util.*;

public final class ActorCommitSystem implements FrameSystem {
    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var gs = (GameState) state;
        var actors = new TreeMap<>(gs.actors().actors());
        for (var e : bus.events()) applyOne(actors, e);
        bus.view(ActorView.class, new ActorView(actors));
    }

    private static void applyOne(Map<ActorId, ActorState> actors, FrameEvent event) {
        if (event instanceof ActorUpserted(var id, var state)) actors.put(id, state);
        else if (event instanceof ActorRemoved(var id)) actors.remove(id);
        else if (event instanceof ActorHealthAdjusted(var id, var delta)) {
            var s = actors.get(id);
            if (s == null) return;
            int max = s.maxHealthHearts();
            int nh = Math.max(0, Math.min(max, s.healthHearts() + delta));
            if (nh != s.healthHearts()) actors.put(id, new ActorState(s.classId(), s.equippedWeaponId(), nh, max));
        }
    }
}