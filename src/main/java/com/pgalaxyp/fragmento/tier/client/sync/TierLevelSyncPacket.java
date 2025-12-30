package com.pgalaxyp.fragmento.tier.client.sync;

import com.pgalaxyp.fragmento.tier.common.network.TierNetworkIds;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TierLevelSyncPacket(
        int level,
        long version
) implements CustomPacketPayload {

    public static final Type<TierLevelSyncPacket> TYPE =
            new Type<>(TierNetworkIds.SYNC);

    public static final StreamCodec<ByteBuf, TierLevelSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    TierLevelSyncPacket::level,
                    ByteBufCodecs.VAR_LONG,
                    TierLevelSyncPacket::version,
                    TierLevelSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}