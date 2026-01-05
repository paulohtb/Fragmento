package com.pgalaxyp.fragmento.rpg_old.skill.config;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Duration;

public interface AbilityConfig {
    Duration castDuration(com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId skillId);
    Duration cooldownDuration(com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId skillId);
    Duration cancelCooldownDuration(com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId skillId);
    Duration actionLockDuration(com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId skillId);
    Duration executionEntityLife(com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId skillId);
}