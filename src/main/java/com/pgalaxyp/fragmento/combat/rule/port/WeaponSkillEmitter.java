package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;

public interface WeaponSkillEmitter {
    void pressSkill(SkillId skillId);

    void cancelSkill(SkillId skillId);
}