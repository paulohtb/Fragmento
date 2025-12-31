package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.rule.port.HitConfirmationBus;
import com.pgalaxyp.fragmento.combat.rule.port.WeaponActionEmitter;

public record WeaponRuntimeBindings(
        WeaponActionEmitter actionEmitter,
        WeaponSkillEmitter skillEmitter,
        HitConfirmationBus hitBus
) {}