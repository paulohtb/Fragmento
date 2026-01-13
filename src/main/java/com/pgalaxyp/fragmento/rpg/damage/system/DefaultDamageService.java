package com.pgalaxyp.fragmento.rpg.damage.system;

import com.pgalaxyp.fragmento.rpg.damage.api.*;
import com.pgalaxyp.fragmento.rpg.damage.domain.*;
import com.pgalaxyp.fragmento.rpg.damage.snapshot.*;

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