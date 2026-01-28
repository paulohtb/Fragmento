package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.event.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import java.util.*;

public final class AbilityEngine implements AbilityPort {
    private final Map<AbilityId, AbilityDefinition> defs;
    private final TargetingService targeting;
    private final AbilityRepository repo = new AbilityRepository();

    public AbilityEngine(Map<AbilityId, AbilityDefinition> defs, TargetingService targeting) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
        this.targeting = Objects.requireNonNull(targeting);
    }

    @Override
    public AbilityOutcome tryExecute(ActorId actorId, AbilityId abilityId, FrameContext frame, GameState state) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);

        AbilityDefinition def = defs.get(abilityId);
        if (def == null) return reject(actorId, abilityId, AbilityRejectReason.UNKNOWN_ABILITY);

        long f = frame.frameId();

        if (repo.activeOf(actorId, f).isPresent()) return reject(actorId, def.id(), AbilityRejectReason.LOCKED);
        if (repo.cooldownActive(actorId, def.id(), f)) return reject(actorId, def.id(), AbilityRejectReason.COOLDOWN);

        TargetResult target = targeting.resolve(new TargetingRequest(actorId, def.targeting()));

        long endExclusive = def.endFrameExclusive(f);
        repo.putActive(actorId, def.id(), f, endExclusive);

        long cdEnd = def.cooldownEndExclusive(f);
        if (cdEnd >= 0) repo.startCooldown(actorId, def.id(), cdEnd);

        AbilitySnapshot snap = new AbilitySnapshot(def.id(), actorId, f, endExclusive);
        return new AbilityOutcome(List.of(new AbilityStarted(snap, def.startEffect(), target, actorId)));
    }

    @Override
    public AbilityOutcome tick(FrameContext frame, GameState state) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        long f = frame.frameId();

        List<FrameEvent> events = repo.evictEndedAtOrBefore(f);
        repo.cleanupCooldowns(f);
        repo.pruneToActors(state.actors().ids());

        return events.isEmpty() ? AbilityOutcome.empty() : new AbilityOutcome(events);
    }

    @Override
    public AbilityViewSnapshot view(Collection<ActorId> actorIds, long frameId) {
        Objects.requireNonNull(actorIds);
        if (frameId < 0) throw new IllegalArgumentException();
        return new AbilityViewSnapshot(repo.activeAll(actorIds, frameId));
    }

    private static AbilityOutcome reject(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason) {
        return new AbilityOutcome(List.of(new AbilityRejected(actorId, abilityId, reason)));
    }
}