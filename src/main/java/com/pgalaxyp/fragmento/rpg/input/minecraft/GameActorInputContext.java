package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.host.api.*;
import com.pgalaxyp.fragmento.rpg.input.bridge.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.world.item.*;
import net.minecraft.client.player.*;

public final class GameActorInputContext implements ActorInputContextProvider {

    private final ItemWeaponBinding weaponBinding;
    private final LocalActorProvider localActorProvider;

    public GameActorInputContext(ItemWeaponBinding weaponBinding, LocalActorProvider localActorProvider) {
        if (weaponBinding == null || localActorProvider == null) {
            throw new IllegalArgumentException();
        }

        this.weaponBinding = weaponBinding;
        this.localActorProvider = localActorProvider;
    }

    @Override
    public Optional<ActorId> localActorId() {
        return localActorProvider.localActorId();
    }

    @Override
    public Optional<WeaponId> weaponInHandId(ActorId actorId, InputSnapshotView snapshot) {
        if (actorId == null || snapshot == null) {
            throw new IllegalArgumentException();
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) {
            return Optional.empty();
        }

        ActorId current = new ActorId(p.getUUID());
        if (!current.equals(actorId)) {
            return Optional.empty();
        }

        ItemStack stack = p.getMainHandItem();
        return weaponBinding.resolve(stack);
    }
}