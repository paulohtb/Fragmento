package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public interface AbilityConfig {
    Duration castDuration(SkillId skillId);
    Duration cooldownDuration(SkillId skillId);
    Duration actionLockDuration(SkillId skillId);
}