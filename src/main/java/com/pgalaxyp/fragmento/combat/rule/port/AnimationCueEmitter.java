package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.animation.AnimationCue;

public interface AnimationCueEmitter {
    void emit(AnimationCue cue);
}