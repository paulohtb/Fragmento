package com.pgalaxyp.fragmento.combat.nContent.flute.skill;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.action.ActionId;
import com.pgalaxyp.fragmento.combat.domain.action.ActionLock;
import com.pgalaxyp.fragmento.combat.domain.action.ActionTiming;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionId;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionSpec;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public final class FluteVortexInfusedSkill implements InfusedSkill {

    private final InfusionSpec spec;

    public FluteVortexInfusedSkill(int ticksPerSecond) {
        Duration duration = Duration.ofSeconds(1.2, ticksPerSecond);
        Duration window = Duration.ofSeconds(0.0, ticksPerSecond);

        ActionDefinition vortexAction = new ActionDefinition(
                new ActionId("flute.infuse.vortex"),
                new ActionTiming(duration, window),
                ActionLock.weaponAndSkills()
        );

        this.spec = new InfusionSpec(
                new InfusionId("flute.vortex"),
                vortexAction,
                Duration.ofSeconds(3.0, ticksPerSecond)
        );
    }

    @Override
    public InfusionSpec infusionSpec() {
        return spec;
    }
}