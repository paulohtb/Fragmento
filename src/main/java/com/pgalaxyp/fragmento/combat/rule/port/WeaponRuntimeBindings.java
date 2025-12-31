package com.pgalaxyp.fragmento.combat.rule.port;

public record WeaponRuntimeBindings(
        WeaponActionEmitter actionEmitter,
        WeaponSkillEmitter skillEmitter,
        HitConfirmationBus hitBus
) {}