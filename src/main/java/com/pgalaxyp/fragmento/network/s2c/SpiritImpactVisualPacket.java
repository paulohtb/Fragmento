package com.pgalaxyp.fragmento.network.s2c;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record SpiritImpactVisualPacket(Vec3 pos) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("fragmento", "spirit_impact_visual");

    public static final Type<SpiritImpactVisualPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SpiritImpactVisualPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeDouble(p.pos.x);
                        buf.writeDouble(p.pos.y);
                        buf.writeDouble(p.pos.z);
                    },
                    buf -> new SpiritImpactVisualPacket(
                            new Vec3(
                                    buf.readDouble(),
                                    buf.readDouble(),
                                    buf.readDouble()
                            )
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}