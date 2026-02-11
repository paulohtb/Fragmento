package com.pgalaxyp.fragmento.combat.flowModule.system;

import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackDefinition;
import com.pgalaxyp.fragmento.combat.basicAttackModule.event.BasicAttackStepTriggered;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageRequest;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameBus;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;
import com.pgalaxyp.fragmento.combat.projectileModule.event.ProjectileSpawned;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingRequest;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Map;
import java.util.Objects;

public record BasicAttackToCombatEffectsSystem(Map<WeaponId, BasicAttackDefinition> defs, TargetingService targeting, DamagePort damage) implements FrameSystem {
    public BasicAttackToCombatEffectsSystem {
        defs = Map.copyOf(Objects.requireNonNull(defs));
        Objects.requireNonNull(targeting);
        Objects.requireNonNull(damage);
    }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);

        for (var ev : bus.events(BasicAttackStepTriggered.class)) {
            var def = defs.get(ev.weaponId());
            if (def == null) continue;
            if (ev.stepIndex() >= def.steps().size()) continue;

            var step = def.steps().get(ev.stepIndex());
            var result = targeting.resolve(new TargetingRequest(ev.actorId(), step.targeting()));
            bus.publish(new ProjectileSpawned(ev.actorId(), step.projectileId(), result.target()));

            var targetActor = result.actorTargetIdOrNull();
            if (targetActor != null) bus.publish(damage.resolve(new DamageRequest(ev.actorId(), targetActor, step.damage())));
        }
    }
}
