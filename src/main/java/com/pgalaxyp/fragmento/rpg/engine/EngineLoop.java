package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingRequested;
import com.pgalaxyp.fragmento.rpg.core.rule.ActionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleFrame;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg.platform.TargetingResolved;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EngineLoop {

    private final IntentQueue intentQueue;
    private final ActionRule actionRule;
    private final ComboRule comboRule;

    public EngineLoop(
            IntentQueue intentQueue,
            ActionRule actionRule,
            ComboRule comboRule
    ) {
        this.intentQueue = intentQueue;
        this.actionRule = actionRule;
        this.comboRule = comboRule;
    }

    public EngineTickResult tick(
            CombatSnapshot snapshot,
            List<TargetingResolved> targetingResolvedBatch
    ) {
        List<TargetingResolved> resolved =
                targetingResolvedBatch == null ? List.of() : targetingResolvedBatch;

        List<StateDelta> collectedDeltas = new ArrayList<>();
        List<DomainEvent> emittedEvents = new ArrayList<>();

        List<TargetingRequested> targetingRequestsPhase1 = new ArrayList<>();
        List<TargetingRequested> targetingRequestsPhase2 = new ArrayList<>();

        List<Intent> intents = intentQueue.drain();

        for (Intent intent : intents) {
            RuleFrame frame =
                    actionRule.evaluate(
                            intent.actorId(),
                            intent.actionDef(),
                            intent.targetingId()
                    );

            collectedDeltas.add(frame.delta());
            emittedEvents.addAll(frame.events());
        }

        for (DomainEvent event : emittedEvents) {
            if (event instanceof TargetingRequested tr) {
                targetingRequestsPhase1.add(tr);
            }
        }

        int phase1EventsCount = emittedEvents.size();

        for (TargetingResolved tr : resolved) {
            RuleFrame frame =
                    comboRule.evaluate(
                            tr.actorId(),
                            tr.nextStep(),
                            tr.nextIndex(),
                            tr.targetingId()
                    );

            collectedDeltas.add(frame.delta());
            emittedEvents.addAll(frame.events());
        }

        for (int i = phase1EventsCount; i < emittedEvents.size(); i++) {
            DomainEvent event = emittedEvents.get(i);
            if (event instanceof TargetingRequested tr) {
                targetingRequestsPhase2.add(tr);
            }
        }

        CombatSnapshot nextSnapshot =
                EngineSnapshotCommit.commit(snapshot, collectedDeltas);

        List<TargetingRequested> allRequests = new ArrayList<>();
        allRequests.addAll(targetingRequestsPhase1);
        allRequests.addAll(targetingRequestsPhase2);

        return new EngineTickResult(
                nextSnapshot,
                Collections.unmodifiableList(emittedEvents),
                Collections.unmodifiableList(allRequests)
        );
    }
}