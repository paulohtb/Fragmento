package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.damageModule.api.DamageRequest;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;

public interface DamagePort {
    DamageApplied resolve(DamageRequest request);
}