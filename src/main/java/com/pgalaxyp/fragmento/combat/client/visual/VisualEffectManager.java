package com.pgalaxyp.fragmento.combat.client.visual;

import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.VisualCuePayload;
import net.minecraft.client.Minecraft;

public final class VisualEffectManager {

    public void onVisualCue(VisualCuePayload payload) {
        if (payload == null || payload.cue() == null) {
            return;
        }

        AnimationCue cue = payload.cue();
        Minecraft.getInstance().execute(
                () -> RenderTypeEffects.spawnCueEffect(cue)
        );
    }
}