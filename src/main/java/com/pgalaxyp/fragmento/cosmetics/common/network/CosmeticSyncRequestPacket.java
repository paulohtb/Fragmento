package com.pgalaxyp.fragmento.cosmetics.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CosmeticSyncRequestPacket() implements CustomPacketPayload {

    public static final CosmeticSyncRequestPacket INSTANCE = new CosmeticSyncRequestPacket();

    public static final Type<CosmeticSyncRequestPacket> TYPE = new Type<>(CosmeticNetworkIds.SYNC_REQUEST);

    public static final StreamCodec<ByteBuf, CosmeticSyncRequestPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}