package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.bridge.*;
import java.util.*;

public final class AbilityInvariantsTest {

    private static final class FakeTargeting implements TargetingWithWorld {
        private final WorldRaycastAccess world = new WorldRaycastAccess() {
            @Override public Optional<ViewRay> viewRay(ActorId casterId) { return Optional.empty(); }
            @Override public Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks) { return Optional.empty(); }
        };
        @Override public WorldRaycastAccess world() { return world; }
        @Override public TargetResult resolve(TargetingContext context) {
            return TargetResult.fallback(new ActorTarget(context.casterId()), TargetingFallback.SELF, context.casterId());
        }
    }

    private static final class FakeEffects implements EffectService {
        @Override public EffectOutcome applyResolved(FrameContext frame, GameState state, EffectId effectId, ActorId source, ActorId target) {
            return EffectOutcome.empty();
        }
    }

    public static void runAll() {
        abilityLocksThroughEndFrame();
        cooldownBlocksUntilEndFrameExclusive();
    }

    private static void abilityLocksThroughEndFrame() {
        AbilityId id = new AbilityId("ability.test");
        var defs = Map.of(id, new AbilityDef(
                id,
                2,
                0,
                new EffectId("effect.test"),
                new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 1.0, TargetingFallback.SELF)
        ));

        AbilityService svc = new AbilityService(defs, new FakeTargeting(), new FakeEffects());
        ActorId actor = new ActorId(UUID.randomUUID());

        GameState s0 = GameState.empty(new FrameContext(0, 0));

        check(svc.tryStart(id, actor, new FrameContext(10, 0), s0).started(), "expected start at 10");
        check(svc.isLocked(actor, 10), "expected locked at 10");
        check(svc.isLocked(actor, 11), "expected locked at 11");
        check(svc.isLocked(actor, 12), "expected locked at 12");
        check(!svc.isLocked(actor, 13), "expected unlocked at 13");

        check(!svc.tryStart(id, actor, new FrameContext(12, 0), s0).started(), "expected no start at 12");
        check(svc.tryStart(id, actor, new FrameContext(13, 0), s0).started(), "expected start at 13");
    }

    private static void cooldownBlocksUntilEndFrameExclusive() {
        AbilityId id = new AbilityId("ability.cooldown");
        var defs = Map.of(id, new AbilityDef(
                id,
                2,
                5,
                new EffectId("effect.test2"),
                new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 1.0, TargetingFallback.SELF)
        ));

        AbilityService svc = new AbilityService(defs, new FakeTargeting(), new FakeEffects());
        ActorId actor = new ActorId(UUID.randomUUID());

        FrameContext f10 = new FrameContext(10, 0);
        GameState s0 = GameState.empty(new FrameContext(0, 0));

        var start = svc.tryStart(id, actor, f10, s0);
        check(start.started(), "expected start with cooldown");

        GameState s1 = com.pgalaxyp.fragmento.combat.engine.commit.StateDeltaApplier.applyAll(
                new GameState(f10, new TreeMap<>(), new TreeMap<>()),
                start.deltas()
        );

        check(s1.cooldownActive(actor, id, 10), "expected cooldown active at 10");
        check(s1.cooldownActive(actor, id, 14), "expected cooldown active at 14");
        check(!s1.cooldownActive(actor, id, 15), "expected cooldown inactive at 15");

        check(!svc.tryStart(id, actor, new FrameContext(14, 0), s1).started(), "expected blocked by cooldown at 14");
    }

    private static void check(boolean cond, String msg) {
        if (!cond) throw new IllegalStateException(msg);
    }

    private AbilityInvariantsTest() {}
}