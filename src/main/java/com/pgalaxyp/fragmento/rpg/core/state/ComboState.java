package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;

public record ComboState(
        ActionId actionId,
        int stepIndex,
        int stepsTotal
) {}