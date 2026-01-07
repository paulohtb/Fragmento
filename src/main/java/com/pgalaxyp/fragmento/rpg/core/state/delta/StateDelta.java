package com.pgalaxyp.fragmento.rpg.core.state.delta;

import java.util.List;

public record StateDelta(
        List<ActionStateDelta> actionDeltas,
        List<ComboStateDelta> comboDeltas
) {}