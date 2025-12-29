package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class CosmeticSlotCodec {

    public static final StreamCodec<ByteBuf, CosmeticSlot> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(CosmeticSlotCodec::fromInt, CosmeticSlot::ordinal);

    private CosmeticSlotCodec() {}

    private static CosmeticSlot fromInt(int ord) {
        CosmeticSlot[] values = CosmeticSlot.values();
        if (ord < 0 || ord >= values.length) {
            return CosmeticSlot.UNKNOWN;
        }
        return values[ord];
    }
}