package com.pgalaxyp.fragmento.rpg.core.rule.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;
import com.pgalaxyp.fragmento.rpg.core.engine.intent.ActionIntent;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActorActionState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboProgressState;
import java.util.Optional;

public final class ComboActionRule {

    private static final long COMBO_TIMEOUT_NANOS = 1_000_000_000L;

    private final ComboSequence sequence;

    public ComboActionRule(ComboSequence sequence) {
        this.sequence = sequence;
    }

    public Optional<ComboStepTriggered> apply(
            ActionIntent intent,
            ActorActionState actions,
            ComboProgressState combos,
            long nowNanos
    ) {
        var actorId = intent.actorId();

        if (!actions.isIdle(actorId)) return Optional.empty();

        if (combos.timedOut(actorId, nowNanos, COMBO_TIMEOUT_NANOS)) {
            combos.reset(actorId);
        }

        var stepIndex = combos.index(actorId);
        var step = sequence.stepAt(stepIndex);

        actions.start(actorId, intent.actionId(), nowNanos);
        combos.advance(actorId, nowNanos);
        actions.clear(actorId);

        return Optional.of(new ComboStepTriggered(
                actorId,
                step.stepId()
        ));
    }
}