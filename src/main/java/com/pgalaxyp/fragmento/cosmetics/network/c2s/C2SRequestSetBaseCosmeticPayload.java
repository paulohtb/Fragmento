package com.pgalaxyp.fragmento.cosmetics.network.c2s;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record C2SRequestSetBaseCosmeticPayload(int slotOrdinal, ResourceLocation cosmeticId) implements CustomPacketPayload {

    public static final Type<C2SRequestSetBaseCosmeticPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "c2s_set_base_cosmetic"));

    public static final StreamCodec<ByteBuf, C2SRequestSetBaseCosmeticPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    C2SRequestSetBaseCosmeticPayload::slotOrdinal,
                    ResourceLocation.STREAM_CODEC,
                    C2SRequestSetBaseCosmeticPayload::cosmeticId,
                    C2SRequestSetBaseCosmeticPayload::new
            );

    public CosmeticSlot slot() {
        CosmeticSlot[] values = CosmeticSlot.values();
        int i = slotOrdinal;
        if (i < 0 || i >= values.length) {
            return null;
        }
        return values[i];
    }

    public CosmeticId cosmetic() {
        return CosmeticId.of(cosmeticId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}