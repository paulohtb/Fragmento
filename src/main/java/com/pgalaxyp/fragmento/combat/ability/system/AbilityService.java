package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.ability.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import java.util.*;

public final class AbilityService {

    public record TickResult(List<AbilityInstance> ended) {
        public TickResult { ended = List.copyOf(Objects.requireNonNull(ended)); }
        public static TickResult empty() { return new TickResult(List.of()); }
    }

    public record StartResult(boolean started, AbilityInstance instance, List<StateDelta> deltas) {
        public StartResult {
            deltas = List.copyOf(Objects.requireNonNull(deltas));
            if (started && instance == null) throw new IllegalArgumentException();
        }
        public static StartResult notStarted() { return new StartResult(false, null, List.of()); }
    }

    private final AbilityStore store = new AbilityStore();
    private final Map<AbilityId, AbilityDef> defs;
    private final TargetingWithWorld targeting;
    private final EffectService effects;

    public AbilityService(Map<AbilityId, AbilityDef> defs, TargetingWithWorld targeting, EffectService effects) {
        this.defs = Map.copyOf(Objects.requireNonNull(defs));
        this.targeting = Objects.requireNonNull(targeting);
        this.effects = Objects.requireNonNull(effects);
    }

    public boolean hasActive(ActorId actorId, long frame) { return store.hasActive(actorId, frame); }

    public boolean isLocked(ActorId actorId, long frame) { return store.hasLocked(actorId, frame); }

    public Optional<AbilityInstance> activeOf(ActorId actorId, long frame) { return store.activeOf(actorId, frame); }

    public TickResult tick(FrameContext frame) {
        long f = frame.frameId();
        List<AbilityInstance> ended = store.endedAt(f);
        store.evictExpired(f);
        return ended.isEmpty() ? TickResult.empty() : new TickResult(ended);
    }

    public List<AbilityInstanceView> activeViews(long frame) {
        List<AbilityInstance> list = store.activeAll(frame);
        if (list.isEmpty()) return List.of();
        List<AbilityInstanceView> out = new ArrayList<>(list.size());
        for (AbilityInstance a : list) out.add(AbilityInstanceView.of(a));
        return List.copyOf(out);
    }

    public StartResult tryStart(AbilityId abilityId, ActorId actorId, FrameContext frame, GameState state) {
        long f = frame.frameId();
        if (store.hasLocked(actorId, f)) return StartResult.notStarted();

        AbilityDef def = defs.get(abilityId);
        if (def == null) return StartResult.notStarted();

        if (state.cooldownActive(actorId, abilityId, f)) return StartResult.notStarted();

        TargetResult tr = targeting.resolve(new TargetingContext(actorId, def.targeting(), targeting.world()));
        ActorId target = tr.actorTargetOpt().orElse(actorId);

        long start = f;
        long end = start + def.durationFrames();
        AbilityInstance instance = new AbilityInstance(abilityId, actorId, start, end);

        store.put(instance);

        List<StateDelta> out = new ArrayList<>();
        out.addAll(effects.applyResolved(frame, state, def.startEffect(), actorId, target).deltas());

        if (def.cooldownFrames() > 0) out.add(new CooldownStarted(actorId, abilityId, start + def.cooldownFrames()));

        return new StartResult(true, instance, List.copyOf(out));
    }
}