package com.pgalaxyp.fragmento.combat.effect.system;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.combat.effect.api.EffectService;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import com.pgalaxyp.fragmento.combat.damage.api.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.system.TargetingContext;
import java.util.*;

public final class EffectEngine {

    private final GameContent content;
    private final DamageService damage;
    private final DamageSnapshotProvider snapshots;
    private final TargetingWithWorld targeting;

    public EffectEngine(
            GameContent content,
            DamageService damage,
            DamageSnapshotProvider snapshots,
            TargetingWithWorld targeting
    ) {
        this.content = content;
        this.damage = damage;
        this.snapshots = snapshots;
        this.targeting = targeting;
    }

    public EffectService.EffectOutcome apply(EffectContext ctx, EffectIntent intent) {
        EffectDef def = content.findEffect(intent.effectId()).orElse(null);
        if (def == null) return EffectService.EffectOutcome.empty();

        TargetingSpec targetingSpec = def.damage().targeting();
        TargetingContext tgtct =
                new TargetingContext(ctx.source(), targetingSpec, targeting.world());

        var targets = targeting.resolve(tgtct);
        return damage.apply(ctx, def.damage(), targets, snapshots);
    }
}