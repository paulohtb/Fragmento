package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.animation.AnimationCue;
import com.pgalaxyp.fragmento.combat.domain.animation.AnimationKey;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public final class WeaponAnimationBridge {

    private final AnimationCueEmitter emitter;

    public WeaponAnimationBridge(AnimationCueEmitter emitter) {
        this.emitter = emitter;
    }

    public void onActionStarted(ActionDefinition action, CombatTime now) {
        if (emitter == null) {
            return;
        }
        emitter.emit(new AnimationCue(
                action.id().value() != null
                        ? new AnimationKey(action.id().value())
                        : null,
                now.ticks()
        ));
    }
}