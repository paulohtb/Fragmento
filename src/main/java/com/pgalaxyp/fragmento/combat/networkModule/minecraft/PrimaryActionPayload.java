package com.pgalaxyp.fragmento.combat.networkModule.minecraft;

import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PrimaryActionPayload() implements CustomPacketPayload {
    public static final PrimaryActionPayload INSTANCE = new PrimaryActionPayload();
    public static final Type<PrimaryActionPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FragmentoPlatform.MODID, "primary_action"));
    public static final StreamCodec<ByteBuf, PrimaryActionPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}