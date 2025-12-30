package com.pgalaxyp.fragmento.cosmetic.common.network;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.network.codec.StreamCodec;

public final class UuidCodec {

    public static final StreamCodec<ByteBuf, UUID> STREAM_CODEC = StreamCodec.of(UuidCodec::write, UuidCodec::read);

    private UuidCodec() {}

    private static void write(ByteBuf buf, UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    private static UUID read(ByteBuf buf) {
        long most = buf.readLong();
        long least = buf.readLong();
        return new UUID(most, least);
    }
}