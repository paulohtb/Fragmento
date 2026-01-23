package com.pgalaxyp.fragmento.combat.targetingModule.system;

import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetResult;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.port.TargetingPort;
import java.util.Objects;

public record DefaultTargetingService(TargetingPort port) implements TargetingService {
    private static final TargetingResolver RESOLVER = new TargetingResolver();
    public DefaultTargetingService { Objects.requireNonNull(port); }
    @Override public TargetResult resolve(TargetingRequest request) { return RESOLVER.resolve(request, port); }
}