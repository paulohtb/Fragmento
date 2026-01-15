package com.pgalaxyp.fragmento.combat.targeting.system;

import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.bridge.*;

public final class DefaultTargetingService implements TargetingWithWorld {

    private final TargetingResolver resolver = new TargetingResolver();
    private WorldRaycastAccess world;

    public void bindWorld(WorldRaycastAccess world) { this.world = world; }

    @Override
    public WorldRaycastAccess world() { return world; }

    @Override
    public TargetResult resolve(TargetingContext context) { return resolver.resolve(context); }
}