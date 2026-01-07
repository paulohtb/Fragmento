package com.pgalaxyp.fragmento.rpg.core.content.bard;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionPayload;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionPriority;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionType;
import com.pgalaxyp.fragmento.rpg.core.domain.action.CancelPolicy;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepDef;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;
import java.util.List;
import java.util.Set;

public final class BardFlute {

    public static WeaponDef create() {
        var timeline = new ActionTimeline(120, 80, 160);

        var interrupts = Set.of(
                InterruptMask.MOVEMENT,
                InterruptMask.DAMAGE,
                InterruptMask.CONTROL
        );

        var combo = new ComboSequence(List.of(
                new ComboStepDef("hit_1", timeline, interrupts),
                new ComboStepDef("hit_2", timeline, interrupts),
                new ComboStepDef("hit_3", timeline, interrupts)
        ));

        var action = new ActionDef(
                new ActionId("bard_flute_combo"),
                ActionType.COMBO,
                ActionPriority.NORMAL,
                timeline,
                CancelPolicy.SAME_OR_LOWER,
                interrupts,
                ActionPayload.combo(combo)
        );

        return new WeaponDef("bard_flute", action);
    }

    private BardFlute() {}
}