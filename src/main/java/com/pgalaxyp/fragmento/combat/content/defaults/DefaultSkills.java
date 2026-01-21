package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.skill.api.*;
import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;

public final class DefaultSkills {
    public static void register(ContentRegistry registry) {
        if (registry == null) throw new IllegalArgumentException();

        registry.skillRule(new SkillRule(
                new SkillId("skill.bard.flute.finisher"),
                DefaultIds.CLASS_BARD,
                DefaultIds.WEAPON_FLUTE,
                DefaultIds.COMBO_FLUTE_BASIC,
                2,
                DefaultIds.ABILITY_FLUTE,
                DefaultIds.ABILITY_FLUTE_FINISHER
        ));
    }

    private DefaultSkills() {}
}