package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.damageModule.api.*;

public interface DamagePort {
    DamageOutcome resolve(DamageRequest request);
}