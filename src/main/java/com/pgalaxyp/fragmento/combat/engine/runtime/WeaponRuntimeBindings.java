package com.pgalaxyp.fragmento.combat.engine.runtime;

public record WeaponRuntimeBindings(
        WeaponActionEmitter actionEmitter,
        WeaponSkillEmitter skillEmitter,
        HitConfirmationBus hitBus
) {}