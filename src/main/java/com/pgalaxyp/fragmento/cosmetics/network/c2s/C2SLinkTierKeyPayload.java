package com.pgalaxyp.fragmento.cosmetics.network.c2s;

import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record C2SLinkTierKeyPayload(String key) implements CustomPacketPayload {

    public static final Type<C2SLinkTierKeyPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "c2s_link_tier_key"));

    public static final StreamCodec<ByteBuf, C2SLinkTierKeyPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8,
        C2SLinkTierKeyPayload::key,
        C2SLinkTierKeyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}