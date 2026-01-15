package com.pgalaxyp.fragmento.combat.input.minecraft;

import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.core.events.intent.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.*;

public final class MCIntentSender implements InputIntentSink {

    @Override
    public void emit(IntentEnvelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException();
        }
        NFWire.sendIntentToServer(envelope);
    }
}