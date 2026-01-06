package com.pgalaxyp.fragmento.rpg.core.domain.action;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;
import java.util.EnumSet;

public record ActionDef(
        ActionId id,
        ActionType type,
        ActionPriority priority,
        ActionTimeline timeline,
        CancelPolicy cancelPolicy,
        EnumSet<InterruptMask> interruptMask,
        ComboSequence combo
) {}