package com.pgalaxyp.fragmento.feature.bard.common.network.packet;

import com.pgalaxyp.fragmento.feature.bard.common.input.BardWeaponUseHandler;
import com.pgalaxyp.fragmento.feature.bard.common.network.BardNetworkRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BardBasicAbilityPacket() implements CustomPacketPayload {

    public static final ResourceLocation ID = BardNetworkRegistry.id("bard_basic");
    public static final Type<BardBasicAbilityPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, BardBasicAbilityPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public BardBasicAbilityPacket decode(RegistryFriendlyByteBuf buf) {
                    return new BardBasicAbilityPacket();
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, BardBasicAbilityPacket value) {
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BardBasicAbilityPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                BardWeaponUseHandler.handleBasic(serverPlayer);
            }
        });
    }
}
