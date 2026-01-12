package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.input.api.ModInputController;
import com.pgalaxyp.fragmento.rpg.input.bridge.ActorContextProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.IntentEmitter;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotProvider;
import com.pgalaxyp.fragmento.rpg.input.system.ClientTickInputClock;
import com.pgalaxyp.fragmento.rpg.input.system.ComboInputSystem;
import com.pgalaxyp.fragmento.rpg.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.rpg.input.system.InputStateMachine;
import com.pgalaxyp.fragmento.rpg.input.system.LocalClientFrameClock;
import com.pgalaxyp.fragmento.rpg.input.system.WeaponInputPolicy;

public final class NeoForgeInputModuleFactory {

    public record ClientModule(MinecraftInputHook hook, VanillaSuppression suppression) {
        public ClientModule {
            if (hook == null || suppression == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static ClientModule createClient(WeaponItemMapping mapping, WeaponInputPolicy weaponPolicy, int primaryDebounceFrames) {
        if (mapping == null || weaponPolicy == null) {
            throw new IllegalArgumentException();
        }
        if (primaryDebounceFrames <= 0) {
            throw new IllegalArgumentException();
        }

        LocalClientFrameClock localClock = new LocalClientFrameClock();

        SnapshotProvider snapshots = new NeoForgeClientSnapshotProvider();
        ActorContextProvider ctx = new NeoForgeClientActorContextProvider(mapping);
        IntentEmitter emitter = new NeoForgeIntentEmitter();

        InputStateMachine sm = new InputStateMachine(primaryDebounceFrames);
        ComboInputSystem combo = new ComboInputSystem();
        InputConsumptionPolicy consume = new InputConsumptionPolicy(weaponPolicy);

        ModInputController controller = new ModInputController(
            sm,
            combo,
            consume,
            new ClientTickInputClock(localClock),
            emitter
        );

        MouseLeftInterceptor left = new MouseLeftInterceptor(controller);
        BlockBreakInterceptor breakInt = new BlockBreakInterceptor(controller);

        MinecraftInputHook hook = new MinecraftInputHook(ctx, left, breakInt);

        VanillaSuppression suppression = new VanillaSuppression(localClock, snapshots, hook);

        return new ClientModule(hook, suppression);
    }

    private NeoForgeInputModuleFactory() {}
}