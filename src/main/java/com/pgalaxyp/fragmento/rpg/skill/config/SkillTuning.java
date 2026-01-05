package com.pgalaxyp.fragmento.rpg.skill.config;

import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;

public record SkillTuning(
        Duration castDuration,
        Duration cooldownDuration,
        Duration cancelCooldownDuration,
        Duration actionLockDuration,
        Duration executionEntityLife
) {}