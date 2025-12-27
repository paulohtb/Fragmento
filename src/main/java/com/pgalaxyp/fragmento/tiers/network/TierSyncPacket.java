package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TierSyncPacket(
        Tier tier,
        long version
) implements CustomPacketPayload {

    public static final Type<TierSyncPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath("fragmento", "tier_sync")
            );

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