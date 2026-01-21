package com.pgalaxyp.fragmento.combat.skill.api;

import com.pgalaxyp.fragmento.combat.core.ids.IdValidation;
import java.util.Objects;

public record SkillId(String value) implements Comparable<SkillId> {
    public SkillId {
        value = IdValidation.normalizedKey(value);
    }

    @Override
    public int compareTo(SkillId id) {
        return value.compareTo(Objects.requireNonNull(id).value);
    }
}