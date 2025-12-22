package com.pgalaxyp.fragmento.cosmetics.network.s2c;

import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record S2CToastPayload(String title, String message) implements CustomPacketPayload {

    public static final Type<S2CToastPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "s2c_toast"));

    public static final StreamCodec<ByteBuf, S2CToastPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            S2CToastPayload::title,
            ByteBufCodecs.STRING_UTF8,
            S2CToastPayload::message,
            S2CToastPayload::new
    );

    public S2CToastPayload {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(message, "message");
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}