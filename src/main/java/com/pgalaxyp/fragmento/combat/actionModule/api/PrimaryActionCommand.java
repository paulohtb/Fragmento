package com.pgalaxyp.fragmento.combat.actionModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameCommand;
import java.util.Objects;

public record PrimaryActionCommand(ActorId actorId, WeaponId weaponId) implements FrameCommand {
    public PrimaryActionCommand {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
    }
}