package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;

public interface AnimationCueEmitter {
    void emit(AnimationCue cue);
}