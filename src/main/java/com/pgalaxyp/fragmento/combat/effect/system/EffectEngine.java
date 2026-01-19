package com.pgalaxyp.fragmento.combat.effect.system;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.damage.api.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import com.pgalaxyp.fragmento.combat.event.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import java.util.*;

public final class EffectEngine implements EffectService {

    private final GameContent content;
    private final DamageService damage;
    private final DamageSnapshotProvider snapshots;
    private final TargetingWithWorld targeting;

    public EffectEngine(GameContent content, DamageService damage, DamageSnapshotProvider snapshots, TargetingWithWorld targeting) {
        this.content = Objects.requireNonNull(content);
        this.damage = Objects.requireNonNull(damage);
        this.snapshots = Objects.requireNonNull(snapshots);
        this.targeting = Objects.requireNonNull(targeting);
    }

    @Override
    public EffectOutcome apply(FrameContext frame, GameState state, EffectIntent intent, ActorId source) {
        EffectDef def = content.effects().effect(intent.effectId()).orElse(null);
        if (def == null) return EffectOutcome.empty();

        DamageSpec spec = def.damage();
        TargetingContext ctx = new TargetingContext(source, spec.targeting(), targeting.world());
        TargetResult target = targeting.resolve(ctx);
        ActorId targetId = target.actorTargetOpt().orElse(null);
        if (targetId == null) return EffectOutcome.empty();

        DamageSnapshot snap = snapshots.snapshot(state, source, targetId);
        DamageResult result = damage.resolve(new DamageRequest(source, targetId, spec), snap);

        List<StateDelta> deltas = List.of(new DamageApplied(targetId, result.finalHearts()));
        if (!def.hasVisual()) return new EffectOutcome(deltas, List.of());

        DomainEvent ev = new EffectVisualEvent(frame.frameId(), 0, intent.effectId(), source, targetId, def.visualLifetimeFrames());
        return new EffectOutcome(deltas, List.of(ev));
    }

    @Override
    public EffectOutcome applyAll(FrameContext frame, GameState state, Iterable<EffectIntent> intents, ActorId source) {
        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();
        for (EffectIntent intent : intents) {
            EffectOutcome out = apply(frame, state, intent, source);
            deltas.addAll(out.deltas());
            events.addAll(out.events());
        }
        return new EffectOutcome(deltas, events);
    }
}