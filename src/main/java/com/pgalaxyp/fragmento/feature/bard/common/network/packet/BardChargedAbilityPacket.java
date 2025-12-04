package com.pgalaxyp.fragmento.feature.bard.common.network.packet;

import com.pgalaxyp.fragmento.feature.bard.common.input.BardWeaponUseHandler;
import com.pgalaxyp.fragmento.feature.bard.common.network.BardNetworkRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BardChargedAbilityPacket() implements CustomPacketPayload {

    public static final ResourceLocation ID = BardNetworkRegistry.id("bard_charged");
    public static final Type<BardChargedAbilityPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, BardChargedAbilityPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public BardChargedAbilityPacket decode(RegistryFriendlyByteBuf buf) {
                    return new BardChargedAbilityPacket();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, BardChargedAbilityPacket value) {
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BardChargedAbilityPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                BardWeaponUseHandler.handleCharged(serverPlayer);
            }
        });
    }
}
