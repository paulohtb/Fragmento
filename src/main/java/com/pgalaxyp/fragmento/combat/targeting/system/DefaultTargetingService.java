package com.pgalaxyp.fragmento.combat.targeting.system;

import com.pgalaxyp.fragmento.combat.targeting.api.TargetResult;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.port.TargetingPort;
import java.util.Objects;

public record DefaultTargetingService(TargetingPort port) implements TargetingService {
    private static final TargetingResolver RESOLVER = new TargetingResolver();
    public DefaultTargetingService { Objects.requireNonNull(port); }
    @Override public TargetResult resolve(TargetingRequest request) { return RESOLVER.resolve(request, port); }
}