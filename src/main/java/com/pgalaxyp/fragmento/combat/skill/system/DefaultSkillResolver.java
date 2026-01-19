package com.pgalaxyp.fragmento.combat.skill.system;

import com.pgalaxyp.fragmento.combat.skill.api.*;

public final class DefaultSkillResolver {
    public static SkillResolver create() { return SkillResolver.none(); }
    private DefaultSkillResolver() {}
}
