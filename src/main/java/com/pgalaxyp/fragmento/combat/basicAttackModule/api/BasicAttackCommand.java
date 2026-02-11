package com.pgalaxyp.fragmento.combat.basicAttackModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameCommand;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Objects;

public record BasicAttackCommand(ActorId actorId, WeaponId weaponId) implements FrameCommand {
    public BasicAttackCommand {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
    }
}