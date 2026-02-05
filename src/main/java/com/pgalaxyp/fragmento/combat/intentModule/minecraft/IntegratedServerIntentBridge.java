package com.pgalaxyp.fragmento.combat.intentModule.minecraft;

import com.pgalaxyp.fragmento.combat.intentModule.api.IntentSinkPort;
import java.util.concurrent.atomic.AtomicReference;

public final class IntegratedServerIntentBridge {
    private static final AtomicReference<IntentSinkPort> REF = new AtomicReference<>();

    public static IntentSinkPort get() { return REF.get(); }
    public static void set(IntentSinkPort sink) { REF.set(sink); }

    private IntegratedServerIntentBridge() {}
}