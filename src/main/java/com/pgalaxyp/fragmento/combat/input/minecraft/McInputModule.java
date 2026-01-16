package com.pgalaxyp.fragmento.combat.input.minecraft;

import com.pgalaxyp.fragmento.combat.host.api.*;
import com.pgalaxyp.fragmento.combat.input.api.*;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.input.system.*;

public final class McInputModule {

    public record ClientModule(McInputHook interceptor) {
        public ClientModule { if (interceptor == null) throw new IllegalArgumentException(); }
        public void register(net.neoforged.bus.api.IEventBus bus) {
            if (bus == null) throw new IllegalArgumentException();
            interceptor.registerClient(bus);
        }
    }

    public static ClientModule createClient(
            ItemWeaponBinding mapping,
            int primaryDebounceFrames,
            LocalActorProvider localActorProvider,
            InputSnapshotProvider snapshots,
            InputIntentSink emitter
    ) {
        if (mapping == null || localActorProvider == null || snapshots == null || emitter == null) throw new IllegalArgumentException();
        if (primaryDebounceFrames <= 0) throw new IllegalArgumentException();
        return new ClientModule(createInterceptor(mapping, primaryDebounceFrames, localActorProvider, snapshots, emitter));
    }

    private static McInputHook createInterceptor(
            ItemWeaponBinding mapping,
            int primaryDebounceFrames,
            LocalActorProvider localActorProvider,
            InputSnapshotProvider snapshots,
            InputIntentSink emitter
    ) {
        ActorInputContextProvider actors = new McActorContext(mapping, localActorProvider);
        InputStateMachine sm = new InputStateMachine(primaryDebounceFrames);
        InputConsumptionPolicy consume = new InputConsumptionPolicy();
        FrameClock clock = new LocalFrameClock();
        InputController controller = new InputController(sm, consume, clock, emitter);
        return new McInputHook(snapshots, actors, clock, controller);
    }

    private McInputModule() {}
}
