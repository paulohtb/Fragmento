package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class CosmeticSlotCodec {

    public static final StreamCodec<ByteBuf, CosmeticSlot> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(
                    CosmeticSlotCodec::fromInt,
                    CosmeticSlot::ordinal
            );

    private CosmeticSlotCodec() {
    }

    private static CosmeticSlot fromInt(int ord) {
        CosmeticSlot[] values = CosmeticSlot.values();
        if (ord < 0 || ord >= values.length) {
            throw new IllegalStateException("Invalid CosmeticSlot ordinal " + ord);
        }
        return values[ord];
    }
}