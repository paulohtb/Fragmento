package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingRequested;
import com.pgalaxyp.fragmento.rpg.core.rule.ActionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleFrame;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import java.util.ArrayList;
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
            EngineTargetingInput targetingInput
    ) {
        List<StateDelta> collectedDeltas = new ArrayList<>();
        List<DomainEvent> collectedEvents = new ArrayList<>();

        List<Intent> intents = intentQueue.drain();

        for (Intent intent : intents) {
            RuleFrame frame =
                    actionRule.evaluate(
                            intent.actorId(),
                            intent.actionDef(),
                            intent.targetingId()
                    );
            collectedDeltas.add(frame.delta());
            collectedEvents.addAll(frame.events());
        }

        List<TargetingRequested> targetingRequests = new ArrayList<>();
        for (DomainEvent e : collectedEvents) {
            if (e instanceof TargetingRequested tr) {
                targetingRequests.add(tr);
            }
        }

        if (targetingInput != null) {
            RuleFrame frame =
                    comboRule.evaluate(
                            targetingInput.actorId(),
                            targetingInput.nextStep(),
                            targetingInput.nextIndex(),
                            targetingInput.targetingId()
                    );
            collectedDeltas.add(frame.delta());
            collectedEvents.addAll(frame.events());
        }

        CombatSnapshot nextSnapshot =
                EngineSnapshotCommit.commit(snapshot, collectedDeltas);

        return new EngineTickResult(
                nextSnapshot,
                collectedEvents,
                targetingRequests
        );
    }
}