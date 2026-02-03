package com.pgalaxyp.fragmento.combat.intentModule.minecraft;

import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import java.util.Objects;
import java.util.function.Supplier;

public final class IntegratedServerIntentSink implements IntentSinkPort {
    private static final IntentSinkPort NOOP = e -> {};
    private final Supplier<IntentSinkPort> lookup;
    private volatile IntentSinkPort last;
    private volatile IntentSinkPort delegate = NOOP;

    public IntegratedServerIntentSink(Supplier<IntentSinkPort> lookup) {
        this.lookup = Objects.requireNonNull(lookup);
    }

    @Override public void enqueue(IntentEnvelope envelope) {
        IntentSinkPort port = lookup.get();
        if (port != last) {
            last = port;
            delegate = port == null ? NOOP : port;
        }
        delegate.enqueue(Objects.requireNonNull(envelope));
    }
}