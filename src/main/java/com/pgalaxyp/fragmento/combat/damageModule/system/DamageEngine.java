package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import java.util.Objects;

record DamageEngine(DamageService damage) implements DamagePort {
    DamageEngine { Objects.requireNonNull(damage); }

    @Override public DamageApplied resolve(DamageRequest request) {
        Objects.requireNonNull(request);
        int hearts = damage.resolveDamageHearts(request);
        if (hearts <= 0) throw new IllegalStateException("DamageService returned non positive damageHearts=" + hearts);
        return new DamageApplied(request.sourceActorId(), request.targetActorId(), request.spec(), hearts);
    }
}