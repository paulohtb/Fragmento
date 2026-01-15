package com.pgalaxyp.fragmento.combat.damage.system;

import com.pgalaxyp.fragmento.combat.damage.api.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.*;

public final class DefaultDamageService implements DamageService {

    private final DamageResolver resolver;

    public DefaultDamageService() {
        this.resolver = new DamageResolver();
    }

    @Override
    public DamageResult resolve(DamageRequest request, DamageSnapshot snapshot) {
        if (request == null || snapshot == null) {
            throw new IllegalArgumentException();
        }

        return resolver.resolve(request, snapshot);
    }
}