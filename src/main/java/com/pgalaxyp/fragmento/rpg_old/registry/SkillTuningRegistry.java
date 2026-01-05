package com.pgalaxyp.fragmento.rpg_old.registry;

import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.skill.config.SkillTuning;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SkillTuningRegistry {

    private final Map<SkillId, SkillTuning> bySkill = new HashMap<>();

    public void register(SkillId skillId, SkillTuning tuning) {
        if (skillId == null || tuning == null) return;
        bySkill.put(skillId, Objects.requireNonNull(tuning));
    }

    public SkillTuning resolve(SkillId skillId) {
        if (skillId == null) return null;
        return bySkill.get(skillId);
    }
}