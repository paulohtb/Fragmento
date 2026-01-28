package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.IntentEnvelope;
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