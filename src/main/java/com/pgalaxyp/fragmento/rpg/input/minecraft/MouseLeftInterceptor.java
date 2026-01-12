package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.input.api.ModInputContext;
import com.pgalaxyp.fragmento.rpg.input.api.ModInputController;
import com.pgalaxyp.fragmento.rpg.input.api.ModInputDecision;
import com.pgalaxyp.fragmento.rpg.input.api.ModInputIntent;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotView;
import java.util.Optional;

public final class MouseLeftInterceptor {

    private final ModInputController controller;

    public MouseLeftInterceptor(ModInputController controller) {
        if (controller == null) {
            throw new IllegalArgumentException();
        }
        this.controller = controller;
    }

    public ModInputDecision onMouseLeft(ActorId actorId, SnapshotView snapshot, Optional<WeaponId> activeWeaponId) {
        if (actorId == null || snapshot == null || activeWeaponId == null) {
            throw new IllegalArgumentException();
        }
        if (activeWeaponId.isPresent() && activeWeaponId.get() == null) {
            throw new IllegalArgumentException();
        }

        ModInputContext ctx = new ModInputContext(actorId, snapshot, activeWeaponId);
        return controller.handle(ctx, ModInputIntent.PRIMARY_ACTION);
    }
}