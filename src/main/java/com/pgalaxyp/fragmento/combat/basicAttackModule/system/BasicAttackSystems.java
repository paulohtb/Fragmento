package com.pgalaxyp.fragmento.combat.basicAttackModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;
import java.util.Objects;

public record BasicAttackSystems(FrameSystem commands, FrameSystem runtime) {
    public BasicAttackSystems {
        Objects.requireNonNull(commands);
        Objects.requireNonNull(runtime);
    }
}
