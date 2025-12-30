package com.pgalaxyp.fragmento.combat.nContent.flute;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.action.ActionId;
import com.pgalaxyp.fragmento.combat.domain.action.ActionLock;
import com.pgalaxyp.fragmento.combat.domain.action.ActionTiming;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboStep;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import java.util.List;

public final class FluteComboFactory {

    private FluteComboFactory() {}

    public static ComboDefinition create(int ticksPerSecond) {
        Duration hit1 = Duration.ofSeconds(0.75, ticksPerSecond);
        Duration hit2 = Duration.ofSeconds(0.75, ticksPerSecond);
        Duration hit3 = Duration.ofSeconds(1.25, ticksPerSecond);

        Duration window = Duration.ofSeconds(0.75, ticksPerSecond);

        ActionDefinition a1 = new ActionDefinition(
                new ActionId("flute.combo.hit1"),
                new ActionTiming(hit1, window),
                ActionLock.weaponAndSkills()
        );

        ActionDefinition a2 = new ActionDefinition(
                new ActionId("flute.combo.hit2"),
                new ActionTiming(hit2, window),
                ActionLock.weaponAndSkills()
        );

        ActionDefinition a3 = new ActionDefinition(
                new ActionId("flute.combo.hit3"),
                new ActionTiming(hit3, window),
                ActionLock.weaponAndSkills()
        );

        return new ComboDefinition(
                "flute.combo",
                List.of(
                        new ComboStep(0, a1),
                        new ComboStep(1, a2),
                        new ComboStep(2, a3)
                )
        );
    }
}