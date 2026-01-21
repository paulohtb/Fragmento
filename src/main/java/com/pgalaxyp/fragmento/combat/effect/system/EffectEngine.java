package com.pgalaxyp.fragmento.combat.effect.system;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.damage.api.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import java.util.*;

public final class EffectEngine implements EffectService {
    private final Map<EffectId, EffectDef> effects;
    private final DamageService damage;
    private final DamageSnapshotProvider snapshots;

    public EffectEngine(Map<EffectId, EffectDef> effects, DamageService damage, DamageSnapshotProvider snapshots) {
        this.effects = Map.copyOf(Objects.requireNonNull(effects));
        this.damage = Objects.requireNonNull(damage);
        this.snapshots = Objects.requireNonNull(snapshots);
    }

    @Override
    public EffectOutcome applyResolved(FrameContext frame, GameState state, EffectId effectId, ActorId source, ActorId target) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        EffectDef def = effects.get(effectId);
        if (def == null) return EffectOutcome.empty();

        DamageSnapshot snap = snapshots.snapshot(state, source, target);
        DamageResult result = damage.resolve(new DamageRequest(source, target, def.damage()), snap);

        return new EffectOutcome(List.of(new DamageApplied(target, result.finalHearts())));
    }
}