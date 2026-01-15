package com.pgalaxyp.fragmento.combat.input.minecraft;

import com.pgalaxyp.fragmento.combat.host.api.LocalActorProvider;
import com.pgalaxyp.fragmento.combat.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.combat.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.input.system.FrameClock;
import com.pgalaxyp.fragmento.combat.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.combat.input.system.InputStateMachine;
import com.pgalaxyp.fragmento.combat.input.system.LocalFrameClock;
import com.pgalaxyp.fragmento.combat.input.api.InputController;

public final class McInputModule {

    public record ClientModule(McInputHook interceptor) {
        public ClientModule {
            if (interceptor == null) {
                throw new IllegalArgumentException();
            }
        }

        public void register(net.neoforged.bus.api.IEventBus bus) {
            if (bus == null) {
                throw new IllegalArgumentException();
            }
            interceptor.registerClient(bus);
        }
    }

    public static ClientModule createClient(ItemWeaponBinding mapping, int primaryDebounceFrames, LocalActorProvider localActorProvider) {
        if (mapping == null || localActorProvider == null) {
            throw new IllegalArgumentException();
        }
        if (primaryDebounceFrames <= 0) {
            throw new IllegalArgumentException();
        }

        McInputHook interceptor = createInterceptor(mapping, primaryDebounceFrames, localActorProvider);

        return new ClientModule(interceptor);
    }

    private static McInputHook createInterceptor(ItemWeaponBinding mapping, int primaryDebounceFrames, LocalActorProvider localActorProvider) {
        InputSnapshotProvider snapshots = new McSnapshotSource();
        ActorInputContextProvider actors = new McActorContext(mapping, localActorProvider);
        InputIntentSink emitter = new McIntentSender();
        InputStateMachine sm = new InputStateMachine(primaryDebounceFrames);
        InputConsumptionPolicy consume = new InputConsumptionPolicy();
        FrameClock clock = new LocalFrameClock();
        InputController controller = new InputController(sm, consume, clock, emitter);

        return new McInputHook(snapshots, actors, clock, controller);
    }

    private McInputModule() {}
}