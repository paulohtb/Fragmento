package com.pgalaxyp.fragmento.cosmetics.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;

public record CosmeticSyncRequestPacket() implements CustomPacketPayload {

    public static final CosmeticSyncRequestPacket INSTANCE = new CosmeticSyncRequestPacket();

    public static final Type<CosmeticSyncRequestPacket> TYPE =
            new Type<>(CosmeticNetworkIds.SYNC_REQUEST);

    public static final StreamCodec<ByteBuf, CosmeticSyncRequestPacket> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}