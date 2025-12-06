package com.pgalaxyp.fragmento.feature.bard.common.network.packet;

import com.pgalaxyp.fragmento.feature.bard.common.input.InstrumentUseHandler;
import com.pgalaxyp.fragmento.feature.bard.common.network.NetworkRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChargedAbilityPacket() implements CustomPacketPayload {

    public static final ResourceLocation ID = NetworkRegistry.id("bard_charged");
    public static final Type<ChargedAbilityPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ChargedAbilityPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ChargedAbilityPacket decode(RegistryFriendlyByteBuf buf) {
                    return new ChargedAbilityPacket();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, ChargedAbilityPacket value) {
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChargedAbilityPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                InstrumentUseHandler.handleCharged(serverPlayer);
            }
        });
    }
}
