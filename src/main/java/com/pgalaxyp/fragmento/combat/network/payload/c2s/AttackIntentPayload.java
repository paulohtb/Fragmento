package com.pgalaxyp.fragmento.combat.network.payload.c2s;

import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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

    private static final StreamCodec<ByteBuf, AttackIntent> INTENT_CODEC =
            ByteBufCodecs.VAR_INT.map(
                    i -> AttackIntent.values()[i],
                    AttackIntent::ordinal
            );

    public static final StreamCodec<ByteBuf, AttackIntentPayload> STREAM_CODEC =
            StreamCodec.composite(
                    INTENT_CODEC,
                    AttackIntentPayload::intent,
                    AttackIntentPayload::new
            );

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