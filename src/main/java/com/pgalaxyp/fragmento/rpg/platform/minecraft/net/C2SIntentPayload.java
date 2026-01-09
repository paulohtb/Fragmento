package com.pgalaxyp.fragmento.rpg.platform.minecraft.net;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import io.netty.buffer.ByteBuf;

public record C2SIntentPayload(byte[] data) implements CustomPacketPayload {

    public static final Type<C2SIntentPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "c2s_intent"));
    public static final StreamCodec<ByteBuf, C2SIntentPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE_ARRAY,
            C2SIntentPayload::data,
            C2SIntentPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}