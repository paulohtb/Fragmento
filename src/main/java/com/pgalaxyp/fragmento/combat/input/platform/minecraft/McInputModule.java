package com.pgalaxyp.fragmento.combat.input.platform.minecraft;

import com.pgalaxyp.fragmento.combat.host.api.*;
import com.pgalaxyp.fragmento.combat.input.api.*;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.input.system.*;

public final class McInputModule {

    public record ClientModule(McInputHook hook) {
        public ClientModule { if (hook == null) throw new IllegalArgumentException(); }
        public void register(net.neoforged.bus.api.IEventBus bus) {
            if (bus == null) throw new IllegalArgumentException();
            hook.registerClient(bus);
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
        ActorInputContextProvider actors = new McActorContext(mapping, localActorProvider);
        InputStateMachine sm = new InputStateMachine(primaryDebounceFrames);
        InputConsumptionPolicy consume = new InputConsumptionPolicy();
        FrameClock clock = new LocalFrameClock();
        InputController controller = new InputController(sm, consume, clock, emitter);
        return new ClientModule(new McInputHook(snapshots, actors, clock, controller));
    }

    private McInputModule() {}
}