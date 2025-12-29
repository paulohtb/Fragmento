package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CosmeticSyncPacket(
        UUID playerId,
        CosmeticLoadout loadout,
        long version
) implements CustomPacketPayload {

    public static final Type<CosmeticSyncPacket> TYPE = new Type<>(CosmeticNetworkIds.SYNC);

    public static final StreamCodec<ByteBuf, CosmeticSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UuidCodec.STREAM_CODEC,
                    CosmeticSyncPacket::playerId,
                    CosmeticLoadoutCodec.STREAM_CODEC,
                    CosmeticSyncPacket::loadout,
                    ByteBufCodecs.VAR_LONG,
                    CosmeticSyncPacket::version,
                    CosmeticSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}