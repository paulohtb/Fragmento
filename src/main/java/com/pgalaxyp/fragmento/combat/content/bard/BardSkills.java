package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.skill.api.*;
import java.util.List;

public final class BardSkills {

    public static List<SkillRule> rules() {
        return List.of(
                new SkillRule(
                        new SkillId("bard.flute.combo"),
                        BardIds.BARD,
                        BardIds.FLUTE,
                        1,
                        BardIds.FLUTE_NOTE,
                        BardIds.FLUTE_NOTE_2
                )
        );
    }

    private BardSkills() {}
}