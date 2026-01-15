package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

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