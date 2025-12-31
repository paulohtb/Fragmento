package com.pgalaxyp.fragmento.combat.domain.input;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;

public record WeaponInput(
        WeaponInputType type,
        SkillId skillId
) {
    public static WeaponInput primaryAttack() {
        return new WeaponInput(WeaponInputType.PRIMARY_ATTACK, null);
    }

    public static WeaponInput skillPress(int skillId) {
        return new WeaponInput(WeaponInputType.SKILL_PRESS, new SkillId(Math.max(0, skillId)));
    }

    public static WeaponInput skillCancel(int skillId) {
        return new WeaponInput(WeaponInputType.SKILL_CANCEL, new SkillId(Math.max(0, skillId)));
    }
}