package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.NeoForgeNetWire;

public final class NeoForgeInputIntentSink implements InputIntentSink {

    @Override
    public void emit(IntentEnvelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException();
        }
        NeoForgeNetWire.sendIntentToServer(envelope);
    }
}