package com.pgalaxyp.fragmento.cosmetic.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

final class CosmeticInfoWireCodec {

    static final StreamCodec<ByteBuf, CosmeticInfoWire> CODEC =
            StreamCodec.composite(
                    CosmeticInfoWireBaseCodec.CODEC,
                    CosmeticInfoWire::base,
                    ByteBufCodecs.STRING_UTF8,
                    CosmeticInfoWire::displayName,
                    CosmeticInfoWire::new
            );

    private CosmeticInfoWireCodec() {}
}