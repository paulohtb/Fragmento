package com.pgalaxyp.fragmento.rpg.core.state.delta;

import java.util.List;

public record DeltaBatch(
        List<StateDelta> deltas
) {}