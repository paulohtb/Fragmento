package com.pgalaxyp.fragmento.combat.domain.combo;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;

public record ComboStep(
        int index,
        ActionDefinition action
) {}