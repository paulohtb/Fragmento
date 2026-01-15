package com.pgalaxyp.fragmento.combat.effect.system;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.damage.api.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.system.*;
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
        if (target.actorTargetOpt().isEmpty()) return EffectOutcome.empty();

        ActorId targetId = target.actorTargetOpt().get();
        DamageSnapshot snap = snapshots.snapshot(state, source, targetId);
        DamageResult result = damage.resolve(new DamageRequest(source, targetId, spec), snap);

        return new EffectOutcome(List.of(new com.pgalaxyp.fragmento.combat.delta.DamageApplied(targetId, result.finalHearts())), List.of());
    }
}