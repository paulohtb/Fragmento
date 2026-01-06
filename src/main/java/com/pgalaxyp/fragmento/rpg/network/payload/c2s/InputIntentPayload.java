package com.pgalaxyp.fragmento.rpg.network.payload.c2s;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record InputIntentPayload(String action) implements CustomPacketPayload {

    public static final Type<InputIntentPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "input_intent"));

    public static final StreamCodec<ByteBuf, InputIntentPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            InputIntentPayload::action,
            InputIntentPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}