package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.actorModule.api.LocalActorProvider;

import java.util.*;

import com.pgalaxyp.fragmento.combat.inputModule.port.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.inputModule.port.InputSnapshotView;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.world.item.*;

public final class McActorContext implements ActorInputContextProvider {

    private final ItemWeaponBinding weaponBinding;
    private final LocalActorProvider localActorProvider;

    public McActorContext(ItemWeaponBinding weaponBinding, LocalActorProvider localActorProvider) {
        if (weaponBinding == null || localActorProvider == null) throw new IllegalArgumentException();
        this.weaponBinding = weaponBinding;
        this.localActorProvider = localActorProvider;
    }

    @Override
    public Optional<ActorId> localActorId() { return localActorProvider.localActorId(); }

    @Override
    public Optional<WeaponId> weaponInHandId(ActorId actorId, InputSnapshotView snapshot) {
        if (actorId == null || snapshot == null) throw new IllegalArgumentException();

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) return Optional.empty();

        if (!new ActorId(p.getUUID()).equals(actorId)) return Optional.empty();
        ItemStack stack = p.getMainHandItem();
        return weaponBinding.resolve(stack);
    }
}