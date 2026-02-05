package com.pgalaxyp.fragmento.combat.intentModule.minecraft;

import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import java.util.Objects;
import java.util.function.Supplier;

public final class IntegratedServerIntentSink implements IntentSinkPort {
    private final Supplier<IntentSinkPort> lookup;

    public IntegratedServerIntentSink(Supplier<IntentSinkPort> lookup) {
        this.lookup = Objects.requireNonNull(lookup);
    }

    @Override public boolean enqueue(IntentEnvelope envelope) {
        Objects.requireNonNull(envelope);
        IntentSinkPort sink = lookup.get();
        return sink != null && sink.enqueue(envelope);
    }
}