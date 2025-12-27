package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CosmeticUnequipRequestPacket(
        CosmeticSlot slot
) implements CustomPacketPayload {

    public static final Type<CosmeticUnequipRequestPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_unequip")
            );

    public static final StreamCodec<ByteBuf, CosmeticUnequipRequestPacket> STREAM_CODEC =
            StreamCodec.composite(
                    CosmeticSlotCodec.STREAM_CODEC,
                    CosmeticUnequipRequestPacket::slot,
                    CosmeticUnequipRequestPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}