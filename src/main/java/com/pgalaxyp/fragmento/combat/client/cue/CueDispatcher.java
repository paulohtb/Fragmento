package com.pgalaxyp.fragmento.combat.client.cue;

import com.pgalaxyp.fragmento.combat.client.visual.RenderTypeEffects;
import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.VisualCuePayload;

public final class CueDispatcher {

    public void dispatch(VisualCuePayload payload) {
        if (payload == null) {
            return;
        }
        AnimationCue cue = payload.cue();
        if (cue == null) {
            return;
        }
        RenderTypeEffects.spawnCueEffect(cue);
    }
}