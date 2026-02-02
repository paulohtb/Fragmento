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
    private final AbilityComboRepository combos;

    AbilityEngine(Map<AbilityId, AbilityDefinition> defs, List<AbilityRule> rules, Map<WeaponId, AbilityId> primaryByWeapon, AbilityActorViewPort actors) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
        this.rules = List.copyOf(Objects.requireNonNull(rules));
        this.primaryByWeapon = Map.copyOf(Objects.requireNonNull(primaryByWeapon));
        this.actors = Objects.requireNonNull(actors);
        this.combos = new AbilityComboRepository(maxStepIndex(this.rules));
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
        var plan = combos.plan(actorId, weaponId, f);
        var actorClass = actors.classIdOf(actorId);
        var resolved = resolve(weaponId, plan.step(), baseAbilityId, actorClass);
        var def = defs.get(resolved);
        if (def == null) return reject(actorId, resolved, AbilityRejectReason.UNKNOWN_ABILITY);
        if (repo.activeOf(actorId, f).isPresent()) return reject(actorId, def.id(), AbilityRejectReason.LOCKED);
        if (repo.cooldownActive(actorId, def.id(), f)) return reject(actorId, def.id(), AbilityRejectReason.COOLDOWN);

        combos.commit(actorId, plan);
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
        var events = repo.evictEndedAtOrBefore(f);
        repo.cleanupCooldowns(f);
        repo.pruneToActors(liveActors);
        return events.isEmpty() ? AbilityOutcome.empty() : new AbilityOutcome(events);
    }

    @Override public AbilityViewSnapshot view(Collection<ActorId> actorIds, long frameId) {
        Objects.requireNonNull(actorIds);
        if (frameId < 0) throw new IllegalArgumentException();
        return new AbilityViewSnapshot(repo.activeAll(actorIds, frameId));
    }

    private AbilityId resolve(WeaponId weaponId, int stepIndex, AbilityId baseAbility, Optional<ClassId> actorClass) {
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(baseAbility);
        Objects.requireNonNull(actorClass);
        if (stepIndex < 0) throw new IllegalArgumentException();
        for (var r : rules) if (matches(r, weaponId, stepIndex, baseAbility, actorClass)) return r.resultAbility();
        return baseAbility;
    }

    private static boolean matches(AbilityRule r, WeaponId weaponIdIn, int stepIndexIn, AbilityId baseAbilityIn, Optional<ClassId> actorClass) {
        Objects.requireNonNull(r);
        Objects.requireNonNull(weaponIdIn);
        Objects.requireNonNull(baseAbilityIn);
        Objects.requireNonNull(actorClass);
        if (stepIndexIn < 0) throw new IllegalArgumentException();

        var weaponId = r.weaponId();
        if (weaponId != null && !weaponId.equals(weaponIdIn)) return false;

        var stepIndex = r.stepIndex();
        if (stepIndex != null && stepIndex != stepIndexIn) return false;

        var baseAbility = r.baseAbility();
        if (baseAbility != null && !baseAbility.equals(baseAbilityIn)) return false;

        var classId = r.classId();
        return classId == null || actorClass.filter(classId::equals).isPresent();
    }

    private static AbilityOutcome reject(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason) {
        return new AbilityOutcome(List.of(new AbilityRejected(actorId, abilityId, reason)));
    }

    private static int maxStepIndex(List<AbilityRule> rules) {
        int max = 0;
        for (var r : rules) {
            Integer s = r.stepIndex();
            if (s != null && s > max) max = s;
        }
        return max;
    }
}