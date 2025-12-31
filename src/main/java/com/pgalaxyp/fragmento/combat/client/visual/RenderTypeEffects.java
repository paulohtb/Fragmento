package com.pgalaxyp.fragmento.combat.client.visual;

import com.pgalaxyp.fragmento.combat.domain.animation.AnimationCue;

public final class RenderTypeEffects {

    private RenderTypeEffects() {}

    public static void spawnCueEffect(AnimationCue cue) {
        if (cue == null || cue.key() == null) {
            return;
        }

        switch (cue.key().id()) {
            case "flute.combo.hit1", "flute.combo.hit2", "flute.combo.hit3" -> {}
        }
    }
}