package com.pgalaxyp.fragmento.network;

import com.pgalaxyp.fragmento.item.AbstractBardWeapon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LeftClickPacket(InteractionHand hand) implements CustomPacketPayload {

    public static final Type<LeftClickPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "normal_ability"));

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LeftClickPacket> CODEC =
            CustomPacketPayload.codec(LeftClickPacket::write, LeftClickPacket::new);

    public LeftClickPacket(RegistryFriendlyByteBuf buf) {
        this( buf.readEnum(InteractionHand.class) );
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeEnum(hand);
    }

    public static void handle(LeftClickPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                AbstractBardWeapon.onAbstractNormalAbility(player, packet.hand());
            }
        });
    }
}