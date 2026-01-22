package com.pgalaxyp.fragmento.combat.skill.api;

import com.pgalaxyp.fragmento.combat.combo.system.ComboService;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.skill.system.DefaultSkillResolver;
import com.pgalaxyp.fragmento.combat.skill.system.SkillResolution;
import java.util.*;

public final class SkillModule {
    private final FrameSystem system;

    private SkillModule(FrameSystem system) {
        this.system = Objects.requireNonNull(system);
    }

    public static SkillModule of(List<SkillRule> rules) {
        Objects.requireNonNull(rules);

        var resolver = DefaultSkillResolver.of(rules);
        var combos = new ComboService();
        var system = new SkillResolution(resolver, combos);

        return new SkillModule(system);
    }

    public FrameSystem system() {
        return system;
    }
}