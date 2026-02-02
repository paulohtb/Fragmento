package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.inputModule.port.ActorInputContextProvider;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class McActorContext implements ActorInputContextProvider {
    private final ItemWeaponBinding weaponBinding;
    private final LocalActorProvider localActorProvider;

    public McActorContext(ItemWeaponBinding weaponBinding, LocalActorProvider localActorProvider) {
        if (weaponBinding == null || localActorProvider == null) throw new IllegalArgumentException();
        this.weaponBinding = weaponBinding;
        this.localActorProvider = localActorProvider;
    }

    @Override public Optional<ActorId> localActorId() { return localActorProvider.localActorId(); }

    @Override public Optional<WeaponId> weaponInHandId(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        LocalPlayer p = Minecraft.getInstance().player;
        if (p == null || !new ActorId(p.getUUID()).equals(actorId)) return Optional.empty();
        return weaponBinding.resolve(p.getMainHandItem());
    }
}