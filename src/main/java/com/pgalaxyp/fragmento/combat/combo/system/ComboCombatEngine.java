package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import java.util.*;

public final class ComboCombatEngine implements ComboCombatPort {
    private final ComboEngine engine;
    private final Map<ComboId, ComboPattern> patterns;

    public ComboCombatEngine(Map<ComboId, ComboPattern> patterns) {
        this.patterns = Map.copyOf(Objects.requireNonNull(patterns));
        this.engine = new ComboEngine();
    }

    @Override
    public ComboCombatResult tryInput(ComboIntent intent, FrameContext frame) {
        ComboPattern p = patterns.get(intent.comboId());
        if (p == null) {
            var r = new ComboEvent.Rejected(intent.actorId(), intent.comboId(), intent.input(), ComboRejectReason.UNKNOWN_COMBO);
            return new ComboCombatResult(ComboOutcome.rejected(r), List.of(r));
        }
        ComboOutcome oc = engine.tryInput(intent, frame, p);
        return new ComboCombatResult(oc, oc.events());
    }

    @Override public ComboTickResult tick(FrameContext frame) { return engine.tick(frame); }

    @Override
    public ComboFrameView view(Collection<ActorId> actorIds, long frameId) {
        return new ComboFrameView(engine.activeAll(frameId), engine.executionStates(actorIds, frameId));
    }
}