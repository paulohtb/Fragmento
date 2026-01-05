package com.pgalaxyp.fragmento.rpg_old.content.profile;

import com.pgalaxyp.fragmento.rpg_old.catalyst.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.engine.catalyst.CatalystEffectAdapter;
import com.pgalaxyp.fragmento.rpg_old.skill.config.SkillTuning;

import java.util.Map;
import java.util.Objects;

public record CatalystProfile(
        CatalystDefinition catalyst,
        CatalystEffectAdapter effects,
        Map<SkillId, SkillTuning> skillTunings
) {
    public CatalystProfile {
        Objects.requireNonNull(catalyst);
        Objects.requireNonNull(effects);
        skillTunings = Map.copyOf(Objects.requireNonNullElseGet(skillTunings, Map::of));
    }

    public CatalystFamilyId family() {
        return catalyst.family();
    }
}