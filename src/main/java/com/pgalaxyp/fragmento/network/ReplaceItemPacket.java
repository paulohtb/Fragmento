package com.pgalaxyp.fragmento.network;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ReplaceItemPacket(Item item, InteractionHand hand) implements CustomPacketPayload {

    public static final Type<ReplaceItemPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "replace_item"));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static final StreamCodec<RegistryFriendlyByteBuf, ReplaceItemPacket> CODEC =
            CustomPacketPayload.codec(ReplaceItemPacket::write, ReplaceItemPacket::new);

    public ReplaceItemPacket(RegistryFriendlyByteBuf buf) {
        this(
                BuiltInRegistries.ITEM.get(buf.readResourceLocation()),
                buf.readEnum(InteractionHand.class)
        );
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(BuiltInRegistries.ITEM.getKey(item));
        buf.writeEnum(hand);
    }

    public static void handle(ReplaceItemPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                player.setItemInHand(packet.hand(), new ItemStack(packet.item()));
            }
        });
    }
}