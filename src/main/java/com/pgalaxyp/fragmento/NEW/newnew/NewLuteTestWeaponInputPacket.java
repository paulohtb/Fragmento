package com.pgalaxyp.fragmento.NEW.newnew;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record NewLuteTestWeaponInputPacket() implements CustomPacketPayload {

    public static final Type<NewLuteTestWeaponInputPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "lute_test_left_click"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NewLuteTestWeaponInputPacket> CODEC =
            CustomPacketPayload.codec(NewLuteTestWeaponInputPacket::write, NewLuteTestWeaponInputPacket::new);

    public NewLuteTestWeaponInputPacket(RegistryFriendlyByteBuf buf) {
        this();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {}

    public static void handle(NewLuteTestWeaponInputPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                NewNewWeaponController.handleLeftClick(player);
            }
        });
    }
}