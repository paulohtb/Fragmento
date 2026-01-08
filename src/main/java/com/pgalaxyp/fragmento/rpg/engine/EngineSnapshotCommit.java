package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.DeltaBatch;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDeltaType;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class EngineSnapshotCommit {

    static Map<Long, ActorState> projectForSecondPass(
            CombatSnapshot snapshot,
            List<DeltaBatch> batches
    ) {
        return applyInternal(snapshot, batches);
    }

    static CombatSnapshot commitOnce(
            CombatSnapshot snapshot,
            List<DeltaBatch> batches
    ) {
        return new CombatSnapshot(applyInternal(snapshot, batches));
    }

    private static Map<Long, ActorState> applyInternal(
            CombatSnapshot snapshot,
            List<DeltaBatch> batches
    ) {
        Map<Long, ActorState> base =
                snapshot == null ? Map.of() : snapshot.actors();

        Map<Long, ActorState> next = new HashMap<>(base);

        if (batches == null || batches.isEmpty()) {
            return next;
        }

        for (DeltaBatch batch : batches) {
            if (batch == null || batch.deltas() == null || batch.deltas().isEmpty()) {
                continue;
            }

            for (StateDelta d : batch.deltas()) {
                if (d == null || d.type() == null) {
                    continue;
                }

                StateDeltaType type = d.type();

                if (type == StateDeltaType.ACTION_SET) {
                    long actorId = d.actorId();
                    ActionId actionId = d.actionId();

                    ActorState cur = next.get(actorId);
                    ComboState combo = cur == null ? null : cur.comboState();

                    next.put(
                            actorId,
                            new ActorState(
                                    actorId,
                                    new ActionState(actorId, actionId),
                                    combo
                            )
                    );
                    continue;
                }

                if (type == StateDeltaType.ACTION_CLEAR) {
                    long actorId = d.actorId();

                    ActorState cur = next.get(actorId);
                    ComboState combo = cur == null ? null : cur.comboState();

                    next.put(
                            actorId,
                            new ActorState(
                                    actorId,
                                    null,
                                    combo
                            )
                    );
                    continue;
                }

                if (type == StateDeltaType.COMBO_SET) {
                    long actorId = d.actorId();
                    ComboStepId stepId = d.stepId();
                    int index = d.index();

                    ActorState cur = next.get(actorId);
                    ActionState action = cur == null ? null : cur.actionState();

                    next.put(
                            actorId,
                            new ActorState(
                                    actorId,
                                    action,
                                    new ComboState(actorId, stepId, index)
                            )
                    );
                    continue;
                }

                if (type == StateDeltaType.COMBO_CLEAR) {
                    long actorId = d.actorId();

                    ActorState cur = next.get(actorId);
                    ActionState action = cur == null ? null : cur.actionState();

                    next.put(
                            actorId,
                            new ActorState(
                                    actorId,
                                    action,
                                    null
                            )
                    );
                }
            }
        }

        return next;
    }
}