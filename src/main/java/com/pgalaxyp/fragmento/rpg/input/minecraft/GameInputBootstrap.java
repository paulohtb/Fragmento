package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.host.api.LocalActorProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.rpg.input.system.FrameClock;
import com.pgalaxyp.fragmento.rpg.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.rpg.input.system.InputStateMachine;
import com.pgalaxyp.fragmento.rpg.input.system.LocalFrameClock;
import com.pgalaxyp.fragmento.rpg.input.api.InputController;

public final class GameInputBootstrap {

    public record ClientModule(VanillaInputInterceptor interceptor) {
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

        VanillaInputInterceptor interceptor = createInterceptor(mapping, primaryDebounceFrames, localActorProvider);

        return new ClientModule(interceptor);
    }

    private static VanillaInputInterceptor createInterceptor(ItemWeaponBinding mapping, int primaryDebounceFrames, LocalActorProvider localActorProvider) {
        InputSnapshotProvider snapshots = new GameInputSnapshotSource();
        ActorInputContextProvider actors = new GameActorInputContext(mapping, localActorProvider);
        InputIntentSink emitter = new GameInputIntentSink();
        InputStateMachine sm = new InputStateMachine(primaryDebounceFrames);
        InputConsumptionPolicy consume = new InputConsumptionPolicy();
        FrameClock clock = new LocalFrameClock();
        InputController controller = new InputController(sm, consume, clock, emitter);

        return new VanillaInputInterceptor(snapshots, actors, clock, controller);
    }

    private GameInputBootstrap() {}
}