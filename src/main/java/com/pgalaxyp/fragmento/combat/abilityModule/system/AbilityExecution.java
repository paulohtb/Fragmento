package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.abilityModule.intent.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import java.util.Objects;

public final class AbilityExecution implements FrameSystem {
    private final AbilityPort abilities;
    private final AbilityResolver resolver;

    public AbilityExecution(AbilityPort abilities, AbilityResolver resolver) {
        this.abilities = Objects.requireNonNull(abilities);
        this.resolver = Objects.requireNonNull(resolver);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        long f = frame.frameId();

        for (IntentEnvelope envelope : bus.intents(IntentEnvelope.class)) {
            ActorId actorId = envelope.actorId();
            Object intent = envelope.intent();

            if (intent instanceof AbilityUseIntent(AbilityId abilityId, WeaponId id)) {
                execute(actorId, id, abilityId, f, frame, state, bus);
                continue;
            }

            if (intent instanceof AbilityPrimaryIntent(WeaponId weaponId)) {
                var base = resolver.primaryAbility(weaponId);
                if (base != null) execute(actorId, weaponId, base, f, frame, state, bus);
            }
        }

        for (FrameEvent e : abilities.tick(frame, state).events()) bus.publish(e);
    }

    private void execute(ActorId actorId, WeaponId weaponId, com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId base, long f, FrameContext frame, GameState state, FrameBus bus) {
        var resolved = resolver.resolve(actorId, weaponId, base, state, f);
        abilities.tryExecute(actorId, resolved, frame, state).events().forEach(bus::publish);
    }
}