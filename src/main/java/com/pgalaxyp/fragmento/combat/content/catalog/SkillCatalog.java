package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.skill.api.*;
import com.pgalaxyp.fragmento.combat.skill.system.DefaultSkillResolver;
import java.util.*;

public final class SkillCatalog {
    private final List<SkillRule> rules;
    private final SkillResolver resolver;

    public SkillCatalog(List<SkillRule> rules) {
        Objects.requireNonNull(rules);
        var copy = new ArrayList<SkillRule>(rules.size());
        for (SkillRule r : rules) copy.add(Objects.requireNonNull(r));
        this.rules = List.copyOf(copy);
        this.resolver = DefaultSkillResolver.of(this.rules);
    }

    public List<SkillRule> rules() { return rules; }
    public SkillResolver resolver() { return resolver; }
}