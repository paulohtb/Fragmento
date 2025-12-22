package com.pgalaxyp.fragmento.network.s2c;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record MinorWindVortexVisualPacket(
        Vec3 pos,
        int loopDuration,
        int gapDuration,
        int loops,
        float sizeXZ
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("fragmento", "minor_wind_vortex_visual");

    public static final Type<MinorWindVortexVisualPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, MinorWindVortexVisualPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeDouble(p.pos.x);
                        buf.writeDouble(p.pos.y);
                        buf.writeDouble(p.pos.z);
                        buf.writeVarInt(p.loopDuration);
                        buf.writeVarInt(p.gapDuration);
                        buf.writeVarInt(p.loops);
                        buf.writeFloat(p.sizeXZ);
                    },
                    buf -> new MinorWindVortexVisualPacket(
                            new Vec3(
                                    buf.readDouble(),
                                    buf.readDouble(),
                                    buf.readDouble()
                            ),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readVarInt(),
                            buf.readFloat()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}