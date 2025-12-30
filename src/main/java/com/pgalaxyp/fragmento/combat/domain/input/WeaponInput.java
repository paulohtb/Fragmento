package com.pgalaxyp.fragmento.combat.domain.input;

public record WeaponInput(
        WeaponInputType type,
        int skillId
) {
    public static WeaponInput primaryAttack() {
        return new WeaponInput(WeaponInputType.PRIMARY_ATTACK, 0);
    }

    public static WeaponInput skillPress(int skillId) {
        return new WeaponInput(WeaponInputType.SKILL_PRESS, Math.max(0, skillId));
    }

    public static WeaponInput skillCancel(int skillId) {
        return new WeaponInput(WeaponInputType.SKILL_CANCEL, Math.max(0, skillId));
    }
}