package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.ability.port.AbilityCombatPort;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import java.util.*;

public final class AbilityCombatEngine implements AbilityCombatPort {

    private final Map<AbilityId, AbilityDef> defs;
    private final TargetingWithWorld targeting;
    private final AbilityRepository repo = new AbilityRepository();

    public AbilityCombatEngine(Map<AbilityId, AbilityDef> defs, TargetingWithWorld targeting) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
        this.targeting = Objects.requireNonNull(targeting);
    }

    @Override
    public AbilityCombatResult tryExecute(AbilityIntent intent, FrameContext frame, GameState state) {
        Objects.requireNonNull(intent);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);

        AbilityDef def = defs.get(intent.abilityId());
        if (def == null) {
            return reject(intent.actorId(), intent.abilityId(), AbilityRejectReason.UNKNOWN_ABILITY);
        }

        long f = frame.frameId();
        ActorId actorId = intent.actorId();

        if (repo.activeOf(actorId, f).isPresent()) {
            return reject(actorId, def.id(), AbilityRejectReason.LOCKED);
        }

        if (repo.cooldownActive(actorId, def.id(), f)) {
            return reject(actorId, def.id(), AbilityRejectReason.COOLDOWN);
        }

        TargetResult target = targeting.resolve(
                new TargetingContext(actorId, def.targeting(), targeting.world())
        );

        ActorId resolvedTarget = target.actorTargetOpt().orElse(null);
        if (resolvedTarget == null) {
            return reject(actorId, def.id(), AbilityRejectReason.INVALID_TARGET);
        }

        long endExclusive = def.endFrameExclusive(f);
        repo.putActive(actorId, def.id(), f, endExclusive);

        long cdEnd = def.cooldownEndExclusive(f);
        if (cdEnd >= 0) {
            repo.startCooldown(actorId, def.id(), cdEnd);
        }

        AbilitySnapshot snap = new AbilitySnapshot(def.id(), actorId, f, endExclusive);

        return new AbilityCombatResult(
                List.of(new AbilityEvent.Started(
                        snap,
                        def.startEffect(),
                        target,
                        actorId,
                        resolvedTarget
                ))
        );
    }

    @Override
    public AbilityCombatResult tick(FrameContext frame, GameState state) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);

        long f = frame.frameId();
        List<AbilityEvent> events = repo.evictEndedAtOrBefore(f);
        repo.cleanupCooldowns(f);

        return events.isEmpty()
                ? AbilityCombatResult.empty()
                : new AbilityCombatResult(events);
    }

    @Override
    public AbilityFrameView view(Collection<ActorId> actorIds, long frameId) {
        Objects.requireNonNull(actorIds);
        if (frameId < 0) throw new IllegalArgumentException();
        return new AbilityFrameView(repo.activeAll(actorIds, frameId));
    }

    private static AbilityCombatResult reject(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason) {
        return new AbilityCombatResult(
                List.of(new AbilityEvent.Rejected(actorId, abilityId, reason))
        );
    }
}