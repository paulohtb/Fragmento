package com.pgalaxyp.fragmento.rpg.skill.config;

import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;

public interface AbilityConfig {
    Duration castDuration(SkillId skillId);
    Duration cooldownDuration(SkillId skillId);
    Duration actionLockDuration(SkillId skillId);
}