package com.pgalaxyp.fragmento.rpg.core.content.bard;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionPriority;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionType;
import com.pgalaxyp.fragmento.rpg.core.domain.action.CancelPolicy;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepDef;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.MagicMissileEffect;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetFilter;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetPolicy;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetRelation;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingRequest;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class BardFlute {

    public static WeaponDef create() {
        var missile = new MagicMissileEffect(4.0, 12.0, 2.5);

        var targeting = new TargetingRequest(
                TargetRelation.ENEMY,
                TargetPolicy.SINGLE,
                Set.of(TargetFilter.LIVING, TargetFilter.VISIBLE)
        );

        var stepTimeline = new ActionTimeline(5, 10, 5);

        var combo = new ComboSequence(List.of(
                new ComboStepDef("hit_1", stepTimeline, missile, targeting),
                new ComboStepDef("hit_2", stepTimeline, missile, targeting),
                new ComboStepDef("hit_3", stepTimeline, missile, targeting)
        ));

        var action = new ActionDef(
                new ActionId("bard_flute_attack"),
                ActionType.COMBO,
                ActionPriority.NORMAL,
                stepTimeline,
                CancelPolicy.SAME_OR_LOWER,
                EnumSet.of(InterruptMask.MOVEMENT, InterruptMask.DAMAGE, InterruptMask.CONTROL),
                combo
        );

        return new WeaponDef("bard_flute", action);
    }
}