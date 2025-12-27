package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CosmeticEquipRequestPacket(
        CosmeticSlot slot,
        CosmeticId cosmeticId
) implements CustomPacketPayload {

    public static final Type<CosmeticEquipRequestPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_equip")
            );

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