package com.pgalaxyp.fragmento.combat.abilityModule.port;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.Collection;

public interface AbilityPort {
    AbilityOutcome tryExecutePrimary(ActorId actorId, WeaponId weaponId, ClassId actorClass, FrameContext frame);
    AbilityOutcome tick(FrameContext frame, Collection<ActorId> liveActors);
    AbilityViewSnapshot view(Collection<ActorId> actorIds, long frameId);
}