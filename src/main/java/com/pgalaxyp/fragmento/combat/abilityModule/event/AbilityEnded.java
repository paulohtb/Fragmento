package com.pgalaxyp.fragmento.combat.abilityModule.event;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilitySnapshot;
import java.util.Objects;

public record AbilityEnded(AbilitySnapshot snapshot) implements FrameEvent {
    public AbilityEnded { Objects.requireNonNull(snapshot); }
}