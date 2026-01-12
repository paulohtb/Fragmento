package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.input.api.ModInputDecision;
import com.pgalaxyp.fragmento.rpg.input.bridge.ActorContextProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotView;
import java.util.Optional;

public final class MinecraftInputHook {

    private final ActorContextProvider contextProvider;
    private final MouseLeftInterceptor mouseLeft;
    private final BlockBreakInterceptor blockBreak;

    public MinecraftInputHook(
            ActorContextProvider contextProvider,
            MouseLeftInterceptor mouseLeft,
            BlockBreakInterceptor blockBreak
    ) {
        if (contextProvider == null || mouseLeft == null || blockBreak == null) {
            throw new IllegalArgumentException();
        }
        this.contextProvider = contextProvider;
        this.mouseLeft = mouseLeft;
        this.blockBreak = blockBreak;
    }

    public Optional<ActorId> localActorId() {
        return contextProvider.localActorId();
    }

    private Optional<WeaponId> weaponInHand(ActorId actorId, SnapshotView snapshot) {
        return contextProvider.weaponInHandId(actorId, snapshot);
    }

    public ModInputDecision onMouseLeft(ActorId actorId, SnapshotView snapshot) {
        if (actorId == null || snapshot == null) {
            throw new IllegalArgumentException();
        }
        return mouseLeft.onMouseLeft(actorId, snapshot, weaponInHand(actorId, snapshot));
    }

    public ModInputDecision onMouseLeft(SnapshotView snapshot) {
        Optional<ActorId> actorId = contextProvider.localActorId();
        if (actorId.isEmpty()) {
            return ModInputDecision.passThrough();
        }
        return onMouseLeft(actorId.get(), snapshot);
    }
}