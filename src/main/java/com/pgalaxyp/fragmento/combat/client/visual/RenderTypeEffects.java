package com.pgalaxyp.fragmento.combat.client.visual;

import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;

public final class RenderTypeEffects {

    private RenderTypeEffects() {}

    public static void spawnCueEffect(AnimationCue cue) {
        if (cue == null || cue.key() == null) {
            return;
        }

        String id = cue.key().id();
        if (id == null) {
            return;
        }

        switch (id) {
            case "flute.combo.hit1":
            case "flute.combo.hit2":
            case "flute.combo.hit3":
                break;
            default:
                break;
        }
    }
}