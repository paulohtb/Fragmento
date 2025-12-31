package com.pgalaxyp.fragmento.combat.domain.skill;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;

public record SkillDefinition(
        SkillId id,
        SkillCategory category,
        CombatSkillKind combatKind
) {}