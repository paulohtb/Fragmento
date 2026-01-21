package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.delta.StateDelta;
import java.util.List;
import java.util.Objects;

public final class NoopWorldCommandPort implements WorldCommandPort {
    @Override
    public void apply(FrameContext frame, GameState state, List<StateDelta> deltas) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(deltas);
    }
}