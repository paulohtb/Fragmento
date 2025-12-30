package com.pgalaxyp.fragmento.cosmetic.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

final class CosmeticInfoWireBaseCodec {

    static final StreamCodec<ByteBuf, CosmeticInfoWireBase> CODEC =
            StreamCodec.composite(
                    CosmeticEntryCodec.ID_CODEC,
                    CosmeticInfoWireBase::id,
                    CosmeticSlotCodec.STREAM_CODEC,
                    CosmeticInfoWireBase::slot,
                    ByteBufCodecs.VAR_INT,
                    CosmeticInfoWireBase::requiredTier,
                    ByteBufCodecs.VAR_INT,
                    CosmeticInfoWireBase::sort,
                    ByteBufCodecs.BOOL,
                    CosmeticInfoWireBase::visibleToSelf,
                    ByteBufCodecs.STRING_UTF8,
                    CosmeticInfoWireBase::modelKey,
                    CosmeticInfoWireBase::new
            );

    private CosmeticInfoWireBaseCodec() {}
}