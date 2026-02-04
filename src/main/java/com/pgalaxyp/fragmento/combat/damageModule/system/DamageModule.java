package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageService;
import java.util.Objects;

public final class DamageModule {
    public static DamagePort create(DamageService damage) {
        return new DamageEngine(Objects.requireNonNull(damage));
    }

    public static DamagePort createDefault() {
        return create(req -> req.spec().baseHearts());
    }

    private DamageModule() {}
}