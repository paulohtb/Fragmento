package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.damageModule.api.DamageRequest;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageResult;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageService;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamageSnapshot;

public final class DefaultDamageService implements DamageService {
    @Override
    public DamageResult resolve(DamageRequest request, DamageSnapshot snapshot) {
        if (request == null || snapshot == null) {
            throw new IllegalArgumentException();
        }

        DamageSpec spec = request.spec();
        int hearts = spec.baseHearts();

        return new DamageResult(hearts, spec.type(), spec.element());
    }
}