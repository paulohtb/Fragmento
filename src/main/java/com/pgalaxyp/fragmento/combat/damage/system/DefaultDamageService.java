package com.pgalaxyp.fragmento.combat.damage.system;

import com.pgalaxyp.fragmento.combat.damage.api.DamageService;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.DamageSnapshot;

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