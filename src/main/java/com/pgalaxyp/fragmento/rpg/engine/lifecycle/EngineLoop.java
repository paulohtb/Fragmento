package com.pgalaxyp.fragmento.rpg.engine.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleDispatcher;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.InterruptIntent;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.*;
import com.pgalaxyp.fragmento.rpg.engine.input.Intent;
import com.pgalaxyp.fragmento.rpg.engine.input.IntentQueue;
import com.pgalaxyp.fragmento.rpg.engine.store.CombatStateStore;
import com.pgalaxyp.fragmento.rpg.engine.store.SnapshotStore;
import com.pgalaxyp.fragmento.rpg.engine.time.TimeSource;
import com.pgalaxyp.fragmento.rpg.platform.api.content.ActionResolver;
import com.pgalaxyp.fragmento.rpg.platform.api.events.WorldQueryGateway;
import java.util.HashMap;
import java.util.Map;

public final class EngineLoop {

    private final TimeSource time;
    private final ActionResolver actions;
    private final RuleDispatcher dispatcher;
    private final WorldQueryGateway world;
    private final CombatStateStore state;
    private final SnapshotStore snapshots;
    private final IntentQueue intents;

    private long requestSeq;

    public EngineLoop(
            TimeSource time,
            ActionResolver actions,
            RuleDispatcher dispatcher,
            WorldQueryGateway world,
            CombatStateStore state,
            SnapshotStore snapshots,
            IntentQueue intents
    ) {
        this.time = time;
        this.actions = actions;
        this.dispatcher = dispatcher;
        this.world = world;
        this.state = state;
        this.snapshots = snapshots;
        this.intents = intents;
    }

    public void submit(Intent intent) {
        intents.offer(intent);
    }

    public void tick() {
        long now = time.nowMillis();
        boolean changed = false;

        Map<Long, ActorState> pending = new HashMap<>();

        while (true) {
            var intentOpt = intents.poll();
            if (intentOpt.isEmpty()) break;

            var intent = intentOpt.get();
            var current = pending.getOrDefault(
                    intent.actorId(),
                    state.getOrInitial(intent.actorId())
            );

            if (intent.type().isPrimary()) {
                var actionOpt = actions.resolve(intent.actorId(), intent.actionId());
                if (actionOpt.isEmpty()) continue;

                ActionDef action = actionOpt.get();
                long requestId = ++requestSeq;

                var frame = dispatcher.applyPrimary(
                        new ActionIntent(intent.actorId(), intent.actionId()),
                        current,
                        action,
                        now,
                        requestId
                );

                if (!frame.consumed()) continue;

                applyDeltas(frame.deltas(), pending, current);
                changed = true;
                continue;
            }

            var frame = dispatcher.applyInterrupt(
                    new InterruptIntent(intent.actorId(), intent.interruptCause()),
                    current,
                    now
            );

            if (!frame.consumed()) continue;

            applyDeltas(frame.deltas(), pending, current);
            changed = true;
        }

        if (!changed) return;

        state.putAll(pending);
        snapshots.nextSnapshot(state);
    }

    private void applyDeltas(
            Iterable<StateDelta> deltas,
            Map<Long, ActorState> pending,
            ActorState base
    ) {
        for (StateDelta delta : deltas) {
            var current = pending.getOrDefault(delta.actorId(), base);

            if (delta instanceof ActionDelta(long id, ActionState nextAction)) {
                pending.put(
                        id,
                        new ActorState(
                                id,
                                nextAction,
                                current.combo()
                        )
                );
            } else if (delta instanceof ComboDelta(long actorId, ComboState nextCombo)) {
                pending.put(
                        actorId,
                        new ActorState(
                                actorId,
                                current.currentAction(),
                                nextCombo
                        )
                );
            }
        }
    }
}