package com.pgalaxyp.fragmento.cosmetic.common.network;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticId;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CosmeticEquipRequestPacket(
        CosmeticId cosmeticId
) implements CustomPacketPayload {

    public static final Type<CosmeticEquipRequestPacket> TYPE =
            new Type<>(CosmeticNetworkIds.EQUIP);

    public static final StreamCodec<ByteBuf, CosmeticEquipRequestPacket> STREAM_CODEC =
            StreamCodec.composite(
                    CosmeticEntryCodec.ID_CODEC, CosmeticEquipRequestPacket::cosmeticId,
                    CosmeticEquipRequestPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}