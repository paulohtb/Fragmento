package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record CosmeticDeltaSyncPacket(
        UUID playerId,
        long rosterVersion,
        CosmeticEntry entry
) implements CustomPacketPayload {

    public static final Type<CosmeticDeltaSyncPacket> TYPE =
            new Type<>(CosmeticNetworkIds.SYNC_DELTA);

    public static final StreamCodec<ByteBuf, CosmeticDeltaSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UuidCodec.STREAM_CODEC, CosmeticDeltaSyncPacket::playerId,
                    ByteBufCodecs.VAR_LONG, CosmeticDeltaSyncPacket::rosterVersion,
                    CosmeticEntryCodec.ENTRY_CODEC, CosmeticDeltaSyncPacket::entry,
                    CosmeticDeltaSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}