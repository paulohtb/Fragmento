package com.pgalaxyp.fragmento.cosmetic.common.network;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CosmeticUnequipRequestPacket(
        CosmeticSlot slot
) implements CustomPacketPayload {

    public static final Type<CosmeticUnequipRequestPacket> TYPE =
            new Type<>(CosmeticNetworkIds.UNEQUIP);

    public static final StreamCodec<ByteBuf, CosmeticUnequipRequestPacket> STREAM_CODEC =
            StreamCodec.composite(
                    CosmeticSlotCodec.STREAM_CODEC, CosmeticUnequipRequestPacket::slot,
                    CosmeticUnequipRequestPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}