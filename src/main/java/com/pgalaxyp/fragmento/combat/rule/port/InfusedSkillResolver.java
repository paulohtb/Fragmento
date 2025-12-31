package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;

public interface InfusedSkillResolver {
    InfusedSkill resolve(int skillId);
}