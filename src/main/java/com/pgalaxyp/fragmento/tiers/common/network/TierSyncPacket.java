package com.pgalaxyp.fragmento.tiers.common.network;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TierSyncPacket(
        Tier tier,
        long version
) implements CustomPacketPayload {

    public static final Type<TierSyncPacket> TYPE = new Type<>(TierNetworkIds.SYNC);

    public static final StreamCodec<ByteBuf, TierSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    TierCodec.STREAM_CODEC,
                    TierSyncPacket::tier,
                    ByteBufCodecs.VAR_LONG,
                    TierSyncPacket::version,
                    TierSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}