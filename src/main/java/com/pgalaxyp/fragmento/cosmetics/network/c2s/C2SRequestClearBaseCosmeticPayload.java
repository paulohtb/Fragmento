package com.pgalaxyp.fragmento.cosmetics.network.c2s;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record C2SRequestClearBaseCosmeticPayload(int slotOrdinal) implements CustomPacketPayload {

    public static final Type<C2SRequestClearBaseCosmeticPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "c2s_clear_base_cosmetic"));

    public static final StreamCodec<ByteBuf, C2SRequestClearBaseCosmeticPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        C2SRequestClearBaseCosmeticPayload::slotOrdinal,
        C2SRequestClearBaseCosmeticPayload::new
    );

    public CosmeticSlot slot() {
        CosmeticSlot[] values = CosmeticSlot.values();
        int i = slotOrdinal;
        if (i < 0 || i >= values.length) return null;
        return values[i];
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}