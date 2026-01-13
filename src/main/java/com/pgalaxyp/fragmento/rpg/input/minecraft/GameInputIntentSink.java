package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.input.bridge.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.*;

public final class GameInputIntentSink implements InputIntentSink {

    @Override
    public void emit(IntentEnvelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException();
        }
        NeoForgeNetWire.sendIntentToServer(envelope);
    }
}