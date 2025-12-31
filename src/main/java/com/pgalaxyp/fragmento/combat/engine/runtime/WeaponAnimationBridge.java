package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.animation.AnimationCue;
import com.pgalaxyp.fragmento.combat.domain.animation.AnimationKey;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.port.AnimationCueEmitter;

public final class WeaponAnimationBridge {

    private final AnimationCueEmitter emitter;

    public WeaponAnimationBridge(AnimationCueEmitter emitter) {
        this.emitter = emitter;
    }

    public void onActionStarted(ActionDefinition action, CombatTime now) {
        if (emitter == null || action == null || now == null) {
            return;
        }

        String id = action.id() != null ? action.id().value() : null;
        AnimationKey key = id != null ? new AnimationKey(id) : null;

        emitter.emit(new AnimationCue(key, now.ticks()));
    }
}