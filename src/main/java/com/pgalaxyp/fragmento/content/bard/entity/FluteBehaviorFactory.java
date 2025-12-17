package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.system.entity.behavior.SpiritBehavior;
import com.pgalaxyp.fragmento.system.skill.SkillMode;

public final class FluteBehaviorFactory {

    private FluteBehaviorFactory() {
    }

    public static SpiritBehavior create(SkillMode mode) {
        if (mode == SkillMode.CHARGED) return new FluteChargedBehavior();
        if (mode == SkillMode.SPECIAL) return new FluteSpecialBehavior();
        return new FluteBasicBehavior();
    }
}