package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;

@FunctionalInterface
public interface DamageWorldCommandPort {
    void apply(DamageApplied damage);
}