package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEventType;
import com.pgalaxyp.fragmento.rpg.core.port.TargetingResolved;
import com.pgalaxyp.fragmento.rpg.core.rule.ActionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.core.spec.CycleSpec;
import com.pgalaxyp.fragmento.rpg.core.spec.FrameOrderSpec;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.DeltaBatch;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class EngineLoop {

    private final CycleSpec cycle;
    private final IntentQueue intentQueue;
    private final WeaponCatalog weaponCatalog;
    private final RulePipeline pipeline;

    public EngineLoop(
            CycleSpec cycle,
            IntentQueue intentQueue,
            WeaponCatalog weaponCatalog,
            ActionRule actionRule,
            ComboRule comboRule
    ) {
        this.cycle = cycle;
        this.intentQueue = intentQueue;
        this.weaponCatalog = weaponCatalog;
        this.pipeline = new RulePipeline(actionRule, comboRule);
    }

    public EngineLoop(
            CycleSpec cycle,
            IntentQueue intentQueue,
            WeaponCatalog weaponCatalog,
            RulePipeline pipeline
    ) {
        this.cycle = cycle;
        this.intentQueue = intentQueue;
        this.weaponCatalog = weaponCatalog;
        this.pipeline = pipeline;
    }

    public EngineTickResult tick(
            CombatSnapshot snapshot,
            List<TargetingResolved> targetingResolvedBatch
    ) {
        Map<Long, ActorState> baseActors = snapshot == null ? Map.of() : snapshot.actors();

        int rulePasses = readRulePasses(cycle);

        RulePipeline.PassResult pass1 =
                pipeline.runIntentPass(
                        cycle,
                        baseActors,
                        intentQueue.drain(),
                        weaponCatalog
                );

        if (rulePasses <= 1) {
            List<DeltaBatch> allBatches = new ArrayList<>(pass1.batches());
            List<DomainEvent> allEvents = new ArrayList<>(pass1.events());

            CombatSnapshot nextSnapshot =
                    EngineSnapshotCommit.commitOnce(
                            snapshot,
                            allBatches
                    );

            List<DomainEvent> targetingRequests = extractTargetingRequests(allEvents);

            return new EngineTickResult(
                    nextSnapshot,
                    Collections.unmodifiableList(allEvents),
                    Collections.unmodifiableList(targetingRequests)
            );
        }

        Map<Long, ActorState> projected =
                EngineSnapshotCommit.projectForSecondPass(
                        snapshot,
                        pass1.batches()
                );

        RulePipeline.PassResult pass2 =
                pipeline.runTargetingPass(
                        cycle,
                        projected,
                        targetingResolvedBatch,
                        weaponCatalog
                );

        List<DeltaBatch> allBatches = new ArrayList<>();
        allBatches.addAll(pass1.batches());
        allBatches.addAll(pass2.batches());

        List<DomainEvent> allEvents = new ArrayList<>();
        allEvents.addAll(pass1.events());
        allEvents.addAll(pass2.events());

        CombatSnapshot nextSnapshot =
                EngineSnapshotCommit.commitOnce(
                        snapshot,
                        allBatches
                );

        List<DomainEvent> targetingRequests = extractTargetingRequests(allEvents);

        return new EngineTickResult(
                nextSnapshot,
                Collections.unmodifiableList(allEvents),
                Collections.unmodifiableList(targetingRequests)
        );
    }

    private static int readRulePasses(CycleSpec cycle) {
        if (cycle == null) {
            return 2;
        }
        FrameOrderSpec fo = cycle.frameOrder();
        if (fo == null) {
            return 2;
        }
        int v = fo.rulePassesPerFrame();
        if (v <= 0) {
            return 2;
        }
        return v;
    }

    private static List<DomainEvent> extractTargetingRequests(List<DomainEvent> allEvents) {
        List<DomainEvent> out = new ArrayList<>();
        if (allEvents == null || allEvents.isEmpty()) {
            return out;
        }
        for (DomainEvent e : allEvents) {
            if (e == null) {
                continue;
            }
            if (e.type() == DomainEventType.TARGETING_REQUESTED) {
                out.add(e);
            }
        }
        return out;
    }
}