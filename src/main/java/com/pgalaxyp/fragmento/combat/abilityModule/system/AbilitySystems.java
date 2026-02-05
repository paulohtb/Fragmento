package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;
import java.util.Objects;

public record AbilitySystems(FrameSystem commands, FrameSystem runtime) {
    public AbilitySystems { Objects.requireNonNull(commands); Objects.requireNonNull(runtime); }
}
