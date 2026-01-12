package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.host.api.LocalActorProvider;
import com.pgalaxyp.fragmento.rpg.input.api.InputController;
import com.pgalaxyp.fragmento.rpg.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.rpg.input.system.ComboInputSystem;
import com.pgalaxyp.fragmento.rpg.input.system.FrameClock;
import com.pgalaxyp.fragmento.rpg.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.rpg.input.system.InputStateMachine;
import com.pgalaxyp.fragmento.rpg.input.system.LocalFrameClock;
import net.neoforged.bus.api.IEventBus;

public final class NeoForgeInputBootstrap {

    public record ClientModule(
            VanillaInputInterceptor interceptor
    ) {
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

    public static ClientModule createClient(
            ItemWeaponBinding mapping,
            int primaryDebounceFrames,
            LocalActorProvider localActorProvider
    ) {
        if (mapping == null || localActorProvider == null) {
            throw new IllegalArgumentException();
        }
        if (primaryDebounceFrames <= 0) {
            throw new IllegalArgumentException();
        }

        InputSnapshotProvider snapshots = new NeoForgeInputSnapshotSource();
        ActorInputContextProvider actors = new NeoForgeActorInputContext(mapping, localActorProvider);
        InputIntentSink emitter = new NeoForgeInputIntentSink();

        InputStateMachine sm = new InputStateMachine(primaryDebounceFrames);
        ComboInputSystem combo = new ComboInputSystem();
        InputConsumptionPolicy consume = new InputConsumptionPolicy();
        FrameClock clock = new LocalFrameClock();

        InputController controller = new InputController(
                sm,
                combo,
                consume,
                clock,
                emitter
        );

        VanillaInputInterceptor interceptor = new VanillaInputInterceptor(
                snapshots,
                actors,
                clock,
                controller
        );

        return new ClientModule(interceptor);
    }

    private NeoForgeInputBootstrap() {}
}