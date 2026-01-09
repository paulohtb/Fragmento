package com.pgalaxyp.fragmento.rpg.platform.minecraft.net;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import io.netty.buffer.ByteBuf;

public record S2CSnapshotPayload(byte[] data) implements CustomPacketPayload {

    public static final Type<S2CSnapshotPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "s2c_snapshot"));
    public static final StreamCodec<ByteBuf, S2CSnapshotPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE_ARRAY,
            S2CSnapshotPayload::data,
            S2CSnapshotPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}