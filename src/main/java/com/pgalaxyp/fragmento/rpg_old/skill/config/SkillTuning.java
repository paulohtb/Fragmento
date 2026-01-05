package com.pgalaxyp.fragmento.rpg_old.skill.config;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Duration;

public record SkillTuning(
        Duration castDuration,
        Duration cooldownDuration,
        Duration cancelCooldownDuration,
        Duration actionLockDuration,
        Duration executionEntityLife
) {}