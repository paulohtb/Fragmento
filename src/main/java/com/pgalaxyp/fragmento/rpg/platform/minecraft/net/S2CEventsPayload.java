package com.pgalaxyp.fragmento.rpg.platform.minecraft.net;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import io.netty.buffer.ByteBuf;

public record S2CEventsPayload(byte[] data) implements CustomPacketPayload {

    public static final Type<S2CEventsPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "s2c_events"));
    public static final StreamCodec<ByteBuf, S2CEventsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE_ARRAY,
            S2CEventsPayload::data,
            S2CEventsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}