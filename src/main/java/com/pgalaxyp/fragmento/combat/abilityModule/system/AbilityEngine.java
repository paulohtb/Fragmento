package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.port.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.abilityModule.event.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.*;

final class AbilityEngine implements AbilityPort {
    private final Map<AbilityId, AbilityDefinition> defs;
    private final List<AbilityRule> rules;
    private final Map<WeaponId, AbilityId> primaryByWeapon;
    private final AbilityActorViewPort actors;
    private final AbilityRepository repo = new AbilityRepository();
    private final AbilityComboRepository combos = new AbilityComboRepository();

    AbilityEngine(Map<AbilityId, AbilityDefinition> defs, List<AbilityRule> rules, Map<WeaponId, AbilityId> primaryByWeapon, AbilityActorViewPort actors) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
        this.rules = List.copyOf(Objects.requireNonNull(rules));
        this.primaryByWeapon = Map.copyOf(Objects.requireNonNull(primaryByWeapon));
        this.actors = Objects.requireNonNull(actors);
    }

    @Override public AbilityOutcome tryExecutePrimary(ActorId actorId, WeaponId weaponId, FrameContext frame) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(frame);
        var base = primaryByWeapon.get(weaponId);
        return base == null ? AbilityOutcome.empty() : tryExecute(actorId, base, weaponId, frame);
    }

    @Override public AbilityOutcome tryExecute(ActorId actorId, AbilityId baseAbilityId, WeaponId weaponId, FrameContext frame) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(baseAbilityId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(frame);
        long f = frame.frameId();
        AbilityId resolved = resolve(actorId, weaponId, baseAbilityId, f);
        AbilityDefinition def = defs.get(resolved);
        if (def == null) return reject(actorId, resolved, AbilityRejectReason.UNKNOWN_ABILITY);
        if (repo.activeOf(actorId, f).isPresent()) return reject(actorId, def.id(), AbilityRejectReason.LOCKED);
        if (repo.cooldownActive(actorId, def.id(), f)) return reject(actorId, def.id(), AbilityRejectReason.COOLDOWN);

        long endExclusive = def.endFrameExclusive(f);
        repo.putActive(actorId, def.id(), f, endExclusive);
        long cdEnd = def.cooldownEndExclusive(f);
        if (cdEnd >= 0) repo.startCooldown(actorId, def.id(), cdEnd);

        return new AbilityOutcome(List.of(new AbilityStarted(new AbilitySnapshot(def.id(), actorId, f, endExclusive))));
    }

    @Override public AbilityOutcome tick(FrameContext frame, Collection<ActorId> liveActors) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(liveActors);
        long f = frame.frameId();
        List<FrameEvent> events = repo.evictEndedAtOrBefore(f);
        repo.cleanupCooldowns(f);
        repo.pruneToActors(liveActors);

        return events.isEmpty() ? AbilityOutcome.empty() : new AbilityOutcome(events);
    }

    @Override public AbilityViewSnapshot view(Collection<ActorId> actorIds, long frameId) {
        Objects.requireNonNull(actorIds);
        if (frameId < 0) throw new IllegalArgumentException();
        return new AbilityViewSnapshot(repo.activeAll(actorIds, frameId));
    }

    private AbilityId resolve(ActorId actorId, WeaponId weaponId, AbilityId baseAbility, long frameId) {
        Optional<ClassId> actorClass = actors.classIdOf(actorId);
        int step = combos.nextStep(actorId, weaponId, frameId);
        for (var r : rules) if (r.matches(weaponId, step, baseAbility, actorClass)) return r.resultAbility();
        return baseAbility;
    }

    private static AbilityOutcome reject(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason) {
        return new AbilityOutcome(List.of(new AbilityRejected(actorId, abilityId, reason)));
    }
}