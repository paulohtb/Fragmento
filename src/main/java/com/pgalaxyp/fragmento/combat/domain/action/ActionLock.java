package com.pgalaxyp.fragmento.combat.domain.action;

public record ActionLock(
        boolean blocksWeaponActions,
        boolean blocksSkills
) {
    public static ActionLock weaponOnly() {
        return new ActionLock(true, false);
    }

    public static ActionLock weaponAndSkills() {
        return new ActionLock(true, true);
    }

    public static ActionLock none() {
        return new ActionLock(false, false);
    }
}