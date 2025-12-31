package com.pgalaxyp.fragmento.combat.content.bard.flute;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;
import com.pgalaxyp.fragmento.combat.rule.port.InfusedSkillResolver;

import java.util.Map;
import java.util.Objects;

public final class FluteInfusedSkillResolver implements InfusedSkillResolver {

    private final Map<SkillId, InfusedSkill> byId;

    public FluteInfusedSkillResolver(Map<SkillId, InfusedSkill> byId) {
        this.byId = Map.copyOf(Objects.requireNonNullElseGet(byId, Map::of));
    }

    @Override
    public InfusedSkill resolve(SkillId skillId) {
        return skillId == null ? null : byId.get(skillId);
    }
}