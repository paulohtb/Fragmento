package com.pgalaxyp.fragmento.cosmetics.network.s2c;

import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record S2COpenCosmeticsScreenPayload() implements CustomPacketPayload {

    public static final Type<S2COpenCosmeticsScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "s2c_open_cosmetics_screen"));

    public static final StreamCodec<ByteBuf, S2COpenCosmeticsScreenPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2COpenCosmeticsScreenPayload decode(ByteBuf buf) {
            return new S2COpenCosmeticsScreenPayload();
        }

        @Override
        public void encode(ByteBuf buf, S2COpenCosmeticsScreenPayload value) {
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}