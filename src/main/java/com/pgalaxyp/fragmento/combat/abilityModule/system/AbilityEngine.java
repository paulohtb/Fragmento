package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.event.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.*;

final class AbilityEngine {
    private final Map<AbilityId, AbilityDefinition> defs;
    private final AbilityRepository repo = new AbilityRepository();

    AbilityEngine(Map<AbilityId, AbilityDefinition> defs) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
    }

    FrameEvent tryStart(ActorId actorId, AbilityId abilityId, long frameId) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
        if (frameId < 0) throw new IllegalArgumentException();
        var def = defs.get(abilityId);
        if (def == null) return new AbilityRejected(actorId, abilityId, AbilityRejectReason.UNKNOWN_ABILITY);
        if (repo.activeOf(actorId, frameId).isPresent()) return new AbilityRejected(actorId, def.id(), AbilityRejectReason.LOCKED);
        if (repo.cooldownActive(actorId, def.id(), frameId)) return new AbilityRejected(actorId, def.id(), AbilityRejectReason.COOLDOWN);
        long endExclusive = def.endFrameExclusive(frameId);
        repo.putActive(actorId, def.id(), frameId, endExclusive);
        def.cooldownEndExclusive(frameId).ifPresent(cdEnd -> repo.startCooldown(actorId, def.id(), cdEnd));
        return new AbilityStarted(new AbilitySnapshot(def.id(), actorId, frameId, endExclusive));
    }

    void tick(long frameId, Set<ActorId> liveActors) {
        Objects.requireNonNull(liveActors);
        if (frameId < 0) throw new IllegalArgumentException();
        repo.evictEndedAtOrBefore(frameId);
        repo.cleanupCooldowns(frameId);
        repo.pruneToActors(liveActors);
    }

    AbilityViewSnapshot view(Set<ActorId> actorIds, long frameId) {
        Objects.requireNonNull(actorIds);
        if (frameId < 0) throw new IllegalArgumentException();
        return new AbilityViewSnapshot(repo.activeAll(actorIds, frameId));
    }
}