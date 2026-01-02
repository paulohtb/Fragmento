package com.pgalaxyp.fragmento.combat.network.payload.c2s;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AbilityIntentPayload(
        AbilityIntent intent
) implements CustomPacketPayload {

    public static final Type<AbilityIntentPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    "fragmento",
                    "ability_intent"
            ));

    public AbilityIntentPayload {
        if (intent == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}