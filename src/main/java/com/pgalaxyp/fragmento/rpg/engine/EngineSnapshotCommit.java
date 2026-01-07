package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ActionStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ComboStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class EngineSnapshotCommit {

    static CombatSnapshot commit(
            CombatSnapshot snapshot,
            List<StateDelta> deltas
    ) {
        Map<Long, ActorState> base =
                snapshot == null ? Map.of() : snapshot.actors();

        Map<Long, ActorState> next = new HashMap<>(base);

        for (StateDelta delta : deltas) {

            for (ActionStateDelta a : delta.actionDeltas()) {
                ActorState cur = next.get(a.actorId());
                ComboState combo = cur == null ? null : cur.comboState();
                next.put(
                        a.actorId(),
                        new ActorState(
                                a.actorId(),
                                new ActionState(a.actorId(), a.actionId()),
                                combo
                        )
                );
            }

            for (ComboStateDelta c : delta.comboDeltas()) {
                ActorState cur = next.get(c.actorId());
                ActionState action = cur == null ? null : cur.actionState();
                next.put(
                        c.actorId(),
                        new ActorState(
                                c.actorId(),
                                action,
                                new ComboState(c.actorId(), c.stepId(), c.index())
                        )
                );
            }
        }

        return new CombatSnapshot(next);
    }
}