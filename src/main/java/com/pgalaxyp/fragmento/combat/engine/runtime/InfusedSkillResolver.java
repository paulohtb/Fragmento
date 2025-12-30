package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;

public interface InfusedSkillResolver {

    InfusedSkill resolve(int skillId);
}
