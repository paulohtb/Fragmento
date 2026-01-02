package com.pgalaxyp.fragmento.combat.network.payload.c2s;

import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AttackIntentPayload(
        AttackIntent intent
) implements CustomPacketPayload {

    public static final Type<AttackIntentPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    "fragmento",
                    "attack_intent"
            ));

    public AttackIntentPayload {
        if (intent == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}