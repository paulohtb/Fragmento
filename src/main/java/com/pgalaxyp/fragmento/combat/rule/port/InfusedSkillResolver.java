package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;

public interface InfusedSkillResolver {
    InfusedSkill resolve(SkillId skillId);
}