package com.pgalaxyp.fragmento.rpg.core.domain.action;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;

public record ActionDef(
        ActionId id,
        ActionType type,
        ActionPriority priority,
        ActionTimeline timeline,
        CancelPolicy cancelPolicy,
        ComboSequence combo
) {
    public boolean hasCombo() {
        return combo != null && combo.size() > 0;
    }
}