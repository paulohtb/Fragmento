package com.pgalaxyp.fragmento.combat.input.platform;

import com.pgalaxyp.fragmento.combat.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.input.port.ServerIntentReceiverPort;
import java.util.Objects;

public final class LocalInputIntentSink implements InputIntentSink {
    private final ServerIntentReceiverPort server;

    public LocalInputIntentSink(ServerIntentReceiverPort server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override
    public void emit(IntentEnvelope envelope) {
        server.enqueue(Objects.requireNonNull(envelope));
    }
}