package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.host.api.*;
import com.pgalaxyp.fragmento.rpg.input.api.*;
import com.pgalaxyp.fragmento.rpg.input.bridge.*;
import com.pgalaxyp.fragmento.rpg.input.system.*;
import net.neoforged.bus.api.IEventBus;

public final class GameInputBootstrap {

    public record ClientModule(VanillaInputInterceptor interceptor) {
        public ClientModule {
            if (interceptor == null) {
                throw new IllegalArgumentException();
            }
        }

        public void register(IEventBus bus) {
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
        ComboInputSystem combo = new ComboInputSystem();
        InputConsumptionPolicy consume = new InputConsumptionPolicy();
        FrameClock clock = new LocalFrameClock();
        InputController controller = new InputController(sm, combo, consume, clock, emitter);

        return new VanillaInputInterceptor(snapshots, actors, clock, controller);
    }

    private GameInputBootstrap() {}
}