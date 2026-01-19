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

    private final AbilityStore store = new AbilityStore();
    private final Map<AbilityId, AbilityDef> defs;
    private final TargetingWithWorld targeting;
    private final EffectService effects;

    public AbilityService(Map<AbilityId, AbilityDef> defs, TargetingWithWorld targeting, EffectService effects) {
        this.defs = Map.copyOf(defs);
        this.targeting = targeting;
        this.effects = effects;
    }

    public boolean hasActive(ActorId actorId, long frame) { return store.hasActive(actorId, frame); }

    public List<StateDelta> tick(FrameContext frame) {
        long f = frame.frameId();
        List<AbilityInstance> ended = store.endedAt(f);
        store.evictExpired(f);
        if (ended.isEmpty()) return List.of();
        List<StateDelta> out = new ArrayList<>(ended.size());
        for (AbilityInstance a : ended) out.add(new AbilityEnded(a.abilityId(), a.actorId(), a.startFrame(), a.endFrame()));
        return List.copyOf(out);
    }

    public List<AbilityInstanceView> activeViews(long frame) {
        List<AbilityInstance> list = store.activeAll(frame);
        if (list.isEmpty()) return List.of();
        List<AbilityInstanceView> out = new ArrayList<>(list.size());
        for (AbilityInstance a : list) out.add(AbilityInstanceView.of(a));
        return List.copyOf(out);
    }

    public List<StateDelta> tryStart(AbilityId abilityId, ActorId actorId, FrameContext frame, GameState state) {
        if (store.hasActive(actorId, frame.frameId())) return List.of();

        AbilityDef def = defs.get(abilityId);
        if (def == null) return List.of();

        TargetResult tr = targeting.resolve(new TargetingContext(actorId, def.targeting(), targeting.world()));
        ActorId target = tr.actorTargetOpt().orElse(actorId);

        long start = frame.frameId();
        long end = start + def.durationFrames();

        store.put(new AbilityInstance(abilityId, actorId, start, end));

        List<StateDelta> out = new ArrayList<>();
        out.add(new AbilityStarted(abilityId, actorId, start, end));
        out.addAll(effects.applyResolved(frame, state, def.startEffect(), actorId, target).deltas());
        return List.copyOf(out);
    }
}
