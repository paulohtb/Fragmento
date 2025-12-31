package com.pgalaxyp.fragmento.combat.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record AttackIntentPayload(UUID playerId, int catalystId)
        implements CustomPacketPayload {

    public static final Type<AttackIntentPayload> TYPE =
            new Type<>(ResourceLocation
                    .fromNamespaceAndPath("fragmento", "attack_intent"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}