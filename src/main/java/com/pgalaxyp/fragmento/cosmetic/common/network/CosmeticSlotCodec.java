package com.pgalaxyp.fragmento.cosmetic.common.network;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class CosmeticSlotCodec {

    public static final StreamCodec<ByteBuf, CosmeticSlot> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(
                    ord -> CosmeticSlot.values()[Math.max(0, Math.min(ord, CosmeticSlot.values().length - 1))],
                    CosmeticSlot::ordinal
            );

    private CosmeticSlotCodec() {}
}