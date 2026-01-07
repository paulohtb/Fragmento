package com.pgalaxyp.fragmento.rpg.core.content.bard;

import com.pgalaxyp.fragmento.rpg.core.domain.action.*;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.*;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.*;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;

import java.util.List;
import java.util.Set;

public final class BardFlute {

    public static WeaponDef create() {
        var missile = new EffectId("magic_missile");

        var targeting = new TargetingRequest(
                TargetRelation.ENEMY,
                TargetPolicy.SINGLE,
                Set.of(TargetFilter.LIVING, TargetFilter.VISIBLE)
        );

        var timeline = new ActionTimeline(120, 80, 160);

        var interrupts = Set.of(
                InterruptMask.MOVEMENT,
                InterruptMask.DAMAGE,
                InterruptMask.CONTROL
        );

        var combo = new ComboSequence(List.of(
                new ComboStepDef("hit_1", timeline, missile, targeting, interrupts),
                new ComboStepDef("hit_2", timeline, missile, targeting, interrupts),
                new ComboStepDef("hit_3", timeline, missile, targeting, interrupts)
        ));

        var action = new ActionDef(
                new ActionId("bard_flute_combo"),
                ActionType.COMBO,
                ActionPriority.NORMAL,
                timeline,
                CancelPolicy.SAME_OR_LOWER,
                combo
        );

        return new WeaponDef("bard_flute", action);
    }
}