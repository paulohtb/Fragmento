package com.pgalaxyp.fragmento.rpg.core.rule.combo;

import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboProgressState;

public record ComboStepTriggered(
        long actorId,
        String stepId,
        ComboProgressState newComboState
) {}