package com.pgalaxyp.fragmento.NEW;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record NormalAbilityInputPacket() implements CustomPacketPayload {

    public static final Type<NormalAbilityInputPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "ability_weapon_normal_ability_input"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NormalAbilityInputPacket> CODEC =
            CustomPacketPayload.codec(NormalAbilityInputPacket::write, NormalAbilityInputPacket::new);

    public NormalAbilityInputPacket(RegistryFriendlyByteBuf buf) {
        this();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {
    }

    public static void handle(NormalAbilityInputPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                NewAbstractWeapon.handleNormalAbilityInput(player);
            }
        });
    }
}
