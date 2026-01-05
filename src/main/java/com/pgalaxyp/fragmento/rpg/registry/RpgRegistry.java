package com.pgalaxyp.fragmento.rpg.registry;

import com.pgalaxyp.fragmento.rpg.catalyst.registry.CatalystRegistry;

public final class RpgRegistry {

    private static final CatalystRegistry CATALYSTS = new CatalystRegistry();
    private static final CatalystProfileRegistry PROFILES = new CatalystProfileRegistry();
    private static final SkillTuningRegistry SKILL_TUNINGS = new SkillTuningRegistry();

    public static CatalystRegistry catalysts() {
        return CATALYSTS;
    }

    public static CatalystProfileRegistry profiles() {
        return PROFILES;
    }

    public static SkillTuningRegistry skillTunings() {
        return SKILL_TUNINGS;
    }

    private RpgRegistry() {}
}