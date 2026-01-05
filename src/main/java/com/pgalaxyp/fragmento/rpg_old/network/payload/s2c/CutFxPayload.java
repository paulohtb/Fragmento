package com.pgalaxyp.fragmento.rpg_old.network.payload.s2c;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CutFxPayload(
        double spawnX,
        double spawnY,
        double spawnZ,
        double aimX,
        double aimY,
        double aimZ,
        int lifeTicks,
        byte orientationOrdinal
) implements CustomPacketPayload {

    public static final Type<CutFxPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "cut_fx"));

    public static final StreamCodec<ByteBuf, CutFxPayload> STREAM_CODEC =
            StreamCodec.of(CutFxPayload::write, CutFxPayload::read);

    private static void write(ByteBuf buf, CutFxPayload payload) {
        ByteBufCodecs.DOUBLE.encode(buf, payload.spawnX());
        ByteBufCodecs.DOUBLE.encode(buf, payload.spawnY());
        ByteBufCodecs.DOUBLE.encode(buf, payload.spawnZ());

        ByteBufCodecs.DOUBLE.encode(buf, payload.aimX());
        ByteBufCodecs.DOUBLE.encode(buf, payload.aimY());
        ByteBufCodecs.DOUBLE.encode(buf, payload.aimZ());

        ByteBufCodecs.VAR_INT.encode(buf, payload.lifeTicks());
        ByteBufCodecs.BYTE.encode(buf, payload.orientationOrdinal());
    }

    private static CutFxPayload read(ByteBuf buf) {
        double sx = ByteBufCodecs.DOUBLE.decode(buf);
        double sy = ByteBufCodecs.DOUBLE.decode(buf);
        double sz = ByteBufCodecs.DOUBLE.decode(buf);

        double ax = ByteBufCodecs.DOUBLE.decode(buf);
        double ay = ByteBufCodecs.DOUBLE.decode(buf);
        double az = ByteBufCodecs.DOUBLE.decode(buf);

        int life = ByteBufCodecs.VAR_INT.decode(buf);
        byte o = ByteBufCodecs.BYTE.decode(buf);

        return new CutFxPayload(sx, sy, sz, ax, ay, az, life, o);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}