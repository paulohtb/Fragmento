package com.pgalaxyp.fragmento.combat.skillModule.api;

import java.util.Objects;

public record SkillId(String value) implements Comparable<SkillId> {
    public SkillId { Objects.requireNonNull(value); }
    @Override public int compareTo(SkillId id) { return value.compareTo(Objects.requireNonNull(id).value); }
}