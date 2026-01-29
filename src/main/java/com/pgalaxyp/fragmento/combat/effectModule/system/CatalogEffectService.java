package com.pgalaxyp.fragmento.combat.effectModule.system;

import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectApplied;
import java.util.*;

public final class CatalogEffectService implements EffectService {
    private final Map<EffectId, EffectDef> effects;

    public CatalogEffectService(Map<EffectId, EffectDef> effects) {
        this.effects = Map.copyOf(Objects.requireNonNull(effects));
    }

    @Override public EffectOutcome applyResolved(FrameContext frame, GameState state, EffectId effectId, ActorId source, ActorId target) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        var def = effects.get(effectId);
        return def == null ? EffectOutcome.empty() : new EffectOutcome(List.of(new EffectApplied(effectId, source, target, def.damage())));
    }
}