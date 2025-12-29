package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CosmeticEquipRequestPacket(
        CosmeticSlot slot,
        CosmeticId cosmeticId
) implements CustomPacketPayload {

    public static final Type<CosmeticEquipRequestPacket> TYPE = new Type<>(CosmeticNetworkIds.EQUIP);

    public static final StreamCodec<ByteBuf, CosmeticEquipRequestPacket> STREAM_CODEC =
            StreamCodec.composite(
                    CosmeticSlotCodec.STREAM_CODEC,
                    CosmeticEquipRequestPacket::slot,
                    CosmeticLoadoutCodec.ID_CODEC,
                    CosmeticEquipRequestPacket::cosmeticId,
                    CosmeticEquipRequestPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}