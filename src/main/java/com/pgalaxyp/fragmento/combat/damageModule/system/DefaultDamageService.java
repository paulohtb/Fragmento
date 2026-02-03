package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import java.util.Objects;

enum DefaultDamageService implements DamageService {
    INSTANCE;

    @Override public DamageResult resolve(DamageRequest request) {
        Objects.requireNonNull(request);
        DamageSpec spec = request.spec();
        return new DamageResult(spec.baseHearts(), spec.type(), spec.element());
    }
}