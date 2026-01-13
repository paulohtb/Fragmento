package com.pgalaxyp.fragmento.rpg.targeting.system;

import com.pgalaxyp.fragmento.rpg.targeting.api.*;

public final class DefaultTargetingService implements TargetingService {

    private final TargetingResolver resolver = new TargetingResolver();

    @Override
    public TargetResult resolve(TargetingContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        return resolver.resolve(context);
    }
}