package com.pgalaxyp.fragmento.rpg.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.core.state.delta.*;

final class SnapshotCommit {

    static GameSnapshot commit(
            GameSnapshot snapshot,
            List<StateDelta> deltas
    ) {
        Map next = new HashMap(snapshot.actors());

        for (StateDelta d : deltas) {

            if (d instanceof ComboStarted cs) {
                next.put(
                        cs.actorId(),
                        new ActorState(
                                cs.actorId(),
                                new ComboState(cs.actionId(), 0, cs.stepsTotal())
                        )
                );
            }

            if (d instanceof ComboAdvanced ca) {
                ActorState cur = (ActorState) next.get(ca.actorId());
                ComboState c = cur.combo();
                next.put(
                        ca.actorId(),
                        new ActorState(
                                ca.actorId(),
                                new ComboState(c.actionId(), ca.stepIndex(), c.stepsTotal())
                        )
                );
            }

            if (d instanceof ComboEnded ce) {
                next.put(
                        ce.actorId(),
                        new ActorState(ce.actorId(), null)
                );
            }
        }

        return new GameSnapshot(next);
    }
}