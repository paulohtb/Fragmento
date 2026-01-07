package com.pgalaxyp.fragmento.rpg.engine.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleDispatcher;
import com.pgalaxyp.fragmento.rpg.core.rule.action.PendingTargeting;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.InterruptIntent;
import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg.engine.input.Intent;
import com.pgalaxyp.fragmento.rpg.engine.input.IntentQueue;
import com.pgalaxyp.fragmento.rpg.engine.store.CombatStateStore;
import com.pgalaxyp.fragmento.rpg.engine.store.SnapshotStore;
import com.pgalaxyp.fragmento.rpg.engine.time.TimeSource;
import com.pgalaxyp.fragmento.rpg.platform.api.content.ActionResolver;
import com.pgalaxyp.fragmento.rpg.platform.api.events.WorldQueryGateway;
import java.util.ArrayList;
import java.util.Optional;

public final class EngineLoop {

    private final TimeSource time;
    private final ActionResolver actions;
    private final RuleDispatcher dispatcher;
    private final WorldQueryGateway world;
    private final CombatStateStore state;
    private final SnapshotStore snapshots;
    private final IntentQueue intents;

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

    public Optional<CombatSnapshot> tick() {
        long now = time.nowMillis();

        boolean changed = false;
        var effects = new ArrayList<EffectState>();

        while (true) {
            var intentOpt = intents.poll();
            if (intentOpt.isEmpty()) break;

            var intent = intentOpt.get();
            var current = state.getOrEmpty(intent.actorId());

            if (intent.type().isPrimary()) {
                var actionOpt = actions.resolve(intent.actorId(), intent.actionId());
                if (actionOpt.isEmpty()) continue;

                ActionDef action = actionOpt.get();

                var frame = dispatcher.applyPrimary(
                        new ActionIntent(intent.actorId(), intent.actionId()),
                        current,
                        action,
                        now
                );

                if (!frame.consumed()) continue;

                var acc = frame;
                for (PendingTargeting pt : frame.targetings()) {
                    var resolution = world.resolveTargeting(pt);
                    acc = dispatcher.resolveTargeting(acc, action, pt, resolution, now);
                }

                state.put(acc.nextState());
                effects.addAll(acc.effects());
                changed = true;
                continue;
            }

            var frame = dispatcher.applyInterrupt(
                    new InterruptIntent(intent.actorId(), intent.interruptCause()),
                    current,
                    now
            );

            if (!frame.consumed()) continue;

            state.put(frame.nextState());
            changed = true;
        }

        if (!changed) return Optional.empty();
        return Optional.of(snapshots.nextSnapshot(state, effects));
    }
}