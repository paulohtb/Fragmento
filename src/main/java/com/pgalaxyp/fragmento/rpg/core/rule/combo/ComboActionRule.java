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
        if (!actions.isIdle(intent.actorId())) return Optional.empty();

        var nextCombos = combos;
        if (combos.timedOut(intent.actorId(), nowNanos, COMBO_TIMEOUT_NANOS)) {
            nextCombos = combos.reset(intent.actorId());
        }

        var step = sequence.stepAt(nextCombos.index(intent.actorId()));

        return Optional.of(new ComboStepTriggered(
                intent.actorId(),
                step.stepId(),
                nextCombos.advance(intent.actorId(), nowNanos)
        ));
    }
}