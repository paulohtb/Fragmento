package com.pgalaxyp.fragmento.combat.content.bard.flute;

import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;
import com.pgalaxyp.fragmento.combat.rule.port.InfusedSkillResolver;

import java.util.Map;

public final class FluteInfusedSkillResolver implements InfusedSkillResolver {

    private final Map<Integer, InfusedSkill> byId;

    public FluteInfusedSkillResolver(Map<Integer, InfusedSkill> byId) {
        this.byId = Map.copyOf(byId);
    }

    @Override
    public InfusedSkill resolve(int skillId) {
        return byId.get(skillId);
    }
}