package com.pgalaxyp.fragmento.rpg.engine.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleDispatcher;
import com.pgalaxyp.fragmento.rpg.core.rule.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg.engine.input.Intent;
import com.pgalaxyp.fragmento.rpg.engine.input.IntentType;
import com.pgalaxyp.fragmento.rpg.engine.store.CombatStateStore;
import com.pgalaxyp.fragmento.rpg.engine.store.SnapshotStore;
import com.pgalaxyp.fragmento.rpg.engine.time.TimeSource;
import java.util.List;
import java.util.Optional;

public final class EngineLoop {

    private final TimeSource time;
    private final RuleDispatcher dispatcher;
    private final CombatStateStore state = new CombatStateStore();
    private final SnapshotStore snapshots = new SnapshotStore();

    public EngineLoop(TimeSource time, RuleDispatcher dispatcher) {
        this.time = time;
        this.dispatcher = dispatcher;
    }

    public Optional<Frame> submit(Intent intent) {
        var now = time.nowMillis();
        var current = state.get(intent.actorId());

        RuleDispatcher.RuleFrame frame;

        if (intent.type() == IntentType.PRIMARY_ACTION) {
            frame = dispatcher.applyPrimary(
                    new ActionIntent(intent.actorId(), intent.actionId()),
                    current,
                    now
            );
        } else {
            frame = dispatcher.applyInterrupt(
                    intent.actorId(),
                    current,
                    now
            );
        }

        if (!frame.consumed()) return Optional.empty();

        state.put(intent.actorId(), frame.nextState());
        var snapshot = snapshots.nextSnapshot(state);

        return Optional.of(new Frame(snapshot, frame.events(), now));
    }

    public record Frame(
            CombatSnapshot snapshot,
            List<RpgEvent> events,
            long now
    ) {}
}