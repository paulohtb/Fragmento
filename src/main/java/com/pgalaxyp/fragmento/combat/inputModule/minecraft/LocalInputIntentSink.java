package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.inputModule.port.InputIntentSink;
import com.pgalaxyp.fragmento.combat.random.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.inputModule.port.ServerIntentReceiverPort;
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