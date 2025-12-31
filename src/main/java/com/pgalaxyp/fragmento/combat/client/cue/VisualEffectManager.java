package com.pgalaxyp.fragmento.combat.client.cue;

import com.pgalaxyp.fragmento.combat.network.payload.s2c.VisualCuePayload;
import net.minecraft.client.Minecraft;

public final class VisualEffectManager {

    private final CueDispatcher dispatcher;

    public VisualEffectManager(CueDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void onVisualCue(final VisualCuePayload payload) {
        if (payload == null) {
            return;
        }

        Minecraft.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                dispatcher.dispatch(payload);
            }
        });
    }
}