package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

final class CosmeticInfoCodec {

    static final StreamCodec<ByteBuf, CosmeticInfo> CODEC =
            CosmeticInfoWireCodec.CODEC.map(
                    CosmeticInfoWire::toModel,
                    CosmeticInfoWire::fromModel
            );

    private CosmeticInfoCodec() {}
}