package com.pgalaxyp.fragmento.combat.targeting.system;

import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.bridge.*;
import java.util.*;

public final class DefaultTargetingService implements TargetingWithWorld {

    private final TargetingResolver resolver = new TargetingResolver();
    private final WorldRaycastAccess world;

    public DefaultTargetingService(WorldRaycastAccess world) { this.world = Objects.requireNonNull(world); }

    @Override
    public WorldRaycastAccess world() { return world; }

    @Override
    public TargetResult resolve(TargetingContext context) { return resolver.resolve(context); }
}
