package com.pgalaxyp.fragmento.combat.network;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CombatSnapshotPayload(CombatSnapshot snapshot)
        implements CustomPacketPayload {

    public static final Type<CombatSnapshotPayload> TYPE =
            new Type<>(ResourceLocation
                    .fromNamespaceAndPath("fragmento", "combat_snapshot"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}