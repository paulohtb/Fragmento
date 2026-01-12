package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.input.bridge.ActorContextProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotView;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public final class NeoForgeClientActorContextProvider implements ActorContextProvider {

    private final WeaponItemMapping mapping;

    public NeoForgeClientActorContextProvider(WeaponItemMapping mapping) {
        if (mapping == null) {
            throw new IllegalArgumentException();
        }
        this.mapping = mapping;
    }

    @Override
    public Optional<ActorId> localActorId() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) {
            return Optional.empty();
        }
        return Optional.of(new ActorId(p.getUUID()));
    }

    @Override
    public Optional<WeaponId> weaponInHandId(ActorId actorId, SnapshotView snapshot) {
        if (actorId == null || snapshot == null) {
            throw new IllegalArgumentException();
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) {
            return Optional.empty();
        }

        ActorId local = new ActorId(p.getUUID());
        if (!local.equals(actorId)) {
            return Optional.empty();
        }

        ItemStack stack = p.getMainHandItem();
        if (stack == null) {
            return Optional.empty();
        }

        return mapping.resolve(stack);
    }
}