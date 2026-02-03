package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.port.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.*;
import java.util.*;

record DamageEngine(DamageService damage) implements DamagePort {
    DamageEngine { Objects.requireNonNull(damage); }

    @Override public DamageOutcome resolve(DamageRequested request) {
        Objects.requireNonNull(request);
        DamageResult result = damage.resolve(new DamageRequest(request.sourceActorId(), request.targetActorId(), request.spec()));
        return new DamageOutcome(List.of(new DamageApplied(request.targetActorId(), result.damageHearts())));
    }
}