package com.pgalaxyp.fragmento.feature.bard_class.common.network.packet;

import com.pgalaxyp.fragmento.feature.bard_class.common.input.InstrumentUseHandler;
import com.pgalaxyp.fragmento.feature.bard_class.common.network.NetworkRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BasicAbilityPacket() implements CustomPacketPayload {

    public static final ResourceLocation ID = NetworkRegistry.id("bard_basic");
    public static final Type<BasicAbilityPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, BasicAbilityPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public BasicAbilityPacket decode(RegistryFriendlyByteBuf buf) {
                    return new BasicAbilityPacket();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, BasicAbilityPacket value) {
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BasicAbilityPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                InstrumentUseHandler.handleBasic(serverPlayer);
            }
        });
    }
}
