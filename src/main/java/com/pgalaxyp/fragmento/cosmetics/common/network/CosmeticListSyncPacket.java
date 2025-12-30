package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CosmeticListSyncPacket(
        UUID playerId,
        int catalogVersion,
        long rosterVersion,
        List<CosmeticEntry> entries
) implements CustomPacketPayload {

    public static final Type<CosmeticListSyncPacket> TYPE =
            new Type<>(CosmeticNetworkIds.SYNC_LIST);

    public static final StreamCodec<ByteBuf, CosmeticListSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UuidCodec.STREAM_CODEC, CosmeticListSyncPacket::playerId,
                    ByteBufCodecs.VAR_INT, CosmeticListSyncPacket::catalogVersion,
                    ByteBufCodecs.VAR_LONG, CosmeticListSyncPacket::rosterVersion,
                    ByteBufCodecs.collection(ArrayList::new, CosmeticEntryCodec.ENTRY_CODEC),
                    CosmeticListSyncPacket::entries,
                    CosmeticListSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}