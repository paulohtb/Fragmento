package com.pgalaxyp.fragmento.combat.content.bard.flute.skill;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.action.ActionLock;
import com.pgalaxyp.fragmento.combat.domain.action.ActionTiming;
import com.pgalaxyp.fragmento.combat.domain.id.ActionId;
import com.pgalaxyp.fragmento.combat.domain.id.InfusionId;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusionSpec;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public final class FluteVortexInfusedSkill implements InfusedSkill {

    private final int ticksPerSecond;

    public FluteVortexInfusedSkill(int ticksPerSecond) {
        this.ticksPerSecond = Math.max(1, ticksPerSecond);
    }

    @Override
    public InfusionSpec infusionSpec() {
        Duration ttl = Duration.ofSeconds(4.0, ticksPerSecond);

        ActionDefinition infusedAction = new ActionDefinition(
                new ActionId("flute.infused.vortex"),
                new ActionTiming(ttl, Duration.ofTicks(0)),
                ActionLock.weaponAndSkills()
        );

        return new InfusionSpec(
                new InfusionId("flute.vortex"),
                infusedAction,
                ttl
        );
    }
}