package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;

public record ComboState(
        long actorId,
        ComboStepId currentStep,
        int index
) {}