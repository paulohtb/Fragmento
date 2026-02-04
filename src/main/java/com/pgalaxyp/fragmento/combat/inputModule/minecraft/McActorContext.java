package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import com.pgalaxyp.fragmento.combat.inputModule.port.ActorInputContextProvider;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class McActorContext implements ActorInputContextProvider {
    private final ItemWeaponBinding weaponBinding;
    private final Supplier<Optional<ActorId>> localActorId;

    public McActorContext(ItemWeaponBinding weaponBinding, Supplier<Optional<ActorId>> localActorId) {
        if (weaponBinding == null || localActorId == null) throw new IllegalArgumentException();
        this.weaponBinding = weaponBinding;
        this.localActorId = localActorId;
    }

    @Override public Optional<ActorId> localActorId() { return localActorId.get(); }

    @Override public Optional<WeaponId> weaponInHandId(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        LocalPlayer p = Minecraft.getInstance().player;
        if (p == null || !p.getUUID().equals(actorId.value())) return Optional.empty();
        return weaponBinding.resolve(p.getMainHandItem());
    }
}