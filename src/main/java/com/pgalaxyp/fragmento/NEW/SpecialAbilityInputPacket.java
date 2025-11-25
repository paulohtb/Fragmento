package com.pgalaxyp.fragmento.NEW;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SpecialAbilityInputPacket(boolean pressed) implements CustomPacketPayload {

    public static final Type<SpecialAbilityInputPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "ability_weapon_special_ability_input"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpecialAbilityInputPacket> CODEC =
            CustomPacketPayload.codec(SpecialAbilityInputPacket::write, SpecialAbilityInputPacket::new);

    public SpecialAbilityInputPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(this.pressed);
    }

    public static void handle(SpecialAbilityInputPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                NewAbstractWeapon.handleSpecialAbilityInputState(player, packet.pressed());
            }
        });
    }
}
