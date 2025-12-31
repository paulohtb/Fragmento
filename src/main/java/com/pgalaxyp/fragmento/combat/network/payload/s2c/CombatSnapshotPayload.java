package com.pgalaxyp.fragmento.combat.network.payload.s2c;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CombatSnapshotPayload(
        CombatSnapshot snapshot
) implements CustomPacketPayload {

    public static final Type<CombatSnapshotPayload> TYPE =
            new Type<>(ResourceLocation
                    .fromNamespaceAndPath("fragmento", "combat_snapshot"));

    public CombatSnapshotPayload {
        if (snapshot == null) {
            throw new IllegalArgumentException("CombatSnapshotPayload sem snapshot");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}