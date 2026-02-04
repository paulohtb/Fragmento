package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.event.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import java.util.*;

final class AbilityEngine implements AbilityPort {
    private final Map<AbilityId, AbilityDefinition> defs;
    private final List<AbilityRule> rules;
    private final Map<WeaponId, AbilityId> primaryByWeapon;
    private final AbilityRepository repo = new AbilityRepository();
    private final AbilityComboRepository combos;

    AbilityEngine(Map<AbilityId, AbilityDefinition> defs, List<AbilityRule> rules, Map<WeaponId, AbilityId> primaryByWeapon, int comboGapFrames) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
        this.rules = List.copyOf(Objects.requireNonNull(rules));
        this.primaryByWeapon = Map.copyOf(Objects.requireNonNull(primaryByWeapon));
        this.combos = new AbilityComboRepository(maxStepIndex(this.rules), comboGapFrames);
    }

    @Override public AbilityOutcome tryExecutePrimary(ActorId actorId, WeaponId weaponId, ClassId actorClass, FrameContext frame) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(actorClass);
        Objects.requireNonNull(frame);
        AbilityId base = primaryByWeapon.get(weaponId);
        return base == null ? AbilityOutcome.empty() : execute(actorId, base, weaponId, actorClass, frame);
    }

    private AbilityOutcome execute(ActorId actorId, AbilityId baseAbilityId, WeaponId weaponId, ClassId actorClass, FrameContext frame) {
        long f = frame.frameId();
        var plan = combos.plan(actorId, weaponId, f);
        AbilityId resolved = resolve(weaponId, plan.step(), baseAbilityId, actorClass);
        AbilityDefinition def = defs.get(resolved);
        if (def == null) return reject(actorId, resolved, AbilityRejectReason.UNKNOWN_ABILITY);
        if (repo.activeOf(actorId, f).isPresent()) return reject(actorId, def.id(), AbilityRejectReason.LOCKED);
        if (repo.cooldownActive(actorId, def.id(), f)) return reject(actorId, def.id(), AbilityRejectReason.COOLDOWN);
        combos.commit(actorId, plan);
        long endExclusive = def.endFrameExclusive(f);
        repo.putActive(actorId, def.id(), f, endExclusive);
        def.cooldownEndExclusive(f).ifPresent(cdEnd -> repo.startCooldown(actorId, def.id(), cdEnd));
        return new AbilityOutcome(List.of(new AbilityStarted(new AbilitySnapshot(def.id(), actorId, f, endExclusive))));
    }

    @Override public AbilityOutcome tick(FrameContext frame, Collection<ActorId> liveActors) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(liveActors);
        long f = frame.frameId();
        repo.evictEndedAtOrBefore(f);
        repo.cleanupCooldowns(f);
        combos.pruneToActors(liveActors);
        repo.pruneToActors(liveActors);
        return AbilityOutcome.empty();
    }

    @Override public AbilityViewSnapshot view(Collection<ActorId> actorIds, long frameId) {
        Objects.requireNonNull(actorIds);
        if (frameId < 0) throw new IllegalArgumentException();
        return new AbilityViewSnapshot(repo.activeAll(actorIds, frameId));
    }

    private AbilityId resolve(WeaponId weaponId, int stepIndex, AbilityId baseAbility, ClassId actorClass) {
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(baseAbility);
        Objects.requireNonNull(actorClass);
        if (stepIndex < 0) throw new IllegalArgumentException();
        for (var r : rules) if (matches(r, weaponId, stepIndex, baseAbility, actorClass)) return r.resultAbility();
        return baseAbility;
    }

    private static boolean matches(AbilityRule r, WeaponId weaponIdIn, int stepIndexIn, AbilityId baseAbilityIn, ClassId actorClass) {
        Objects.requireNonNull(r);
        Objects.requireNonNull(weaponIdIn);
        Objects.requireNonNull(baseAbilityIn);
        Objects.requireNonNull(actorClass);
        if (stepIndexIn < 0) throw new IllegalArgumentException();
        WeaponId weaponId = r.weaponId();
        if (weaponId != null && !weaponId.equals(weaponIdIn)) return false;
        Integer stepIndex = r.stepIndex();
        if (stepIndex != null && stepIndex != stepIndexIn) return false;
        AbilityId baseAbility = r.baseAbility();
        if (baseAbility != null && !baseAbility.equals(baseAbilityIn)) return false;
        ClassId classId = r.classId();
        return classId == null || classId.equals(actorClass);
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