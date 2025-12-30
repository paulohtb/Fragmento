package com.pgalaxyp.fragmento.combat.domain.skill;

public record SkillDefinition(
        SkillId id,
        SkillCategory category,
        CombatSkillKind combatKind
) {}