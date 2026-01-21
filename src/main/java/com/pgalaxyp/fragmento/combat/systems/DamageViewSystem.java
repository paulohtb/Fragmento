package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.delta.DamageApplied;
import com.pgalaxyp.fragmento.combat.flow.*;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.DamageFrameView;
import java.util.*;

public final class DamageViewSystem implements FrameSystem {

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        List<ActorId> out = collectDamagedActors(bus.deltas());
        bus.view(DamageFrameView.class, out.isEmpty() ? DamageFrameView.empty() : new DamageFrameView(out));
    }

    private static List<ActorId> collectDamagedActors(List<com.pgalaxyp.fragmento.combat.delta.StateDelta> deltas) {
        if (deltas == null || deltas.isEmpty()) return List.of();

        var out = new ArrayList<ActorId>();
        for (var d : deltas) {
            if (d instanceof DamageApplied da) {
                out.add(da.targetActorId());
            }
        }
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }
}