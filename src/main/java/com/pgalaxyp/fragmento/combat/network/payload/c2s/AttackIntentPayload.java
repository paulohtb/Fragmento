package com.pgalaxyp.fragmento.combat.network.payload.c2s;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystId;
import com.pgalaxyp.fragmento.combat.domain.id.PlayerId;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AttackIntentPayload(
        PlayerId playerId,
        CatalystId catalystId
) implements CustomPacketPayload {

    public static final Type<AttackIntentPayload> TYPE =
            new Type<>(ResourceLocation
                    .fromNamespaceAndPath("fragmento", "attack_intent"));

    public AttackIntentPayload {
        if (playerId == null) {
            throw new IllegalArgumentException("AttackIntent sem playerId");
        }
        if (catalystId == null) {
            throw new IllegalArgumentException("AttackIntent sem catalystId");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}