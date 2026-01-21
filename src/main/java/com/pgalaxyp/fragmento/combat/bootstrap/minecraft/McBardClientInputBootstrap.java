package com.pgalaxyp.fragmento.combat.bootstrap.minecraft;

import com.pgalaxyp.fragmento.combat.content.bard.BardInputBindings;
import com.pgalaxyp.fragmento.combat.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.combat.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.combat.input.system.PrimaryActionInputHandler;
import java.util.Objects;

public final class McBardClientInputBootstrap {
    public static PrimaryActionInputHandler createPrimaryHandler(ActorInputContextProvider actorContext, InputSnapshotProvider snapshots, InputIntentSink sink) {
        Objects.requireNonNull(actorContext);
        Objects.requireNonNull(snapshots);
        Objects.requireNonNull(sink);

        return new PrimaryActionInputHandler(actorContext, snapshots, sink, new InputConsumptionPolicy(), BardInputBindings.primaryAbilityByWeapon());
    }

    private McBardClientInputBootstrap() {}
}