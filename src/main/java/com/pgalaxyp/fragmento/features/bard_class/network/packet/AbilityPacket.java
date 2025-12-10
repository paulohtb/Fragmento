package com.pgalaxyp.fragmento.features.bard_class.network.packet;

import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import com.pgalaxyp.fragmento.features.bard_class.network.NetworkRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AbilityPacket(int abilityIndex, int targetId) implements CustomPacketPayload {

    public static final ResourceLocation ID = NetworkRegistry.id("ability");
    public static final Type<AbilityPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeInt(p.abilityIndex());
                        buf.writeInt(p.targetId());
                    },
                    buf -> new AbilityPacket(buf.readInt(), buf.readInt())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AbilityPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) return;

            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof InstrumentBase instrument)) return;

            Entity e = player.level().getEntity(packet.targetId());
            if (!(e instanceof LivingEntity target)) return;
            if (!target.isAlive()) return;

            if (!player.hasLineOfSight(target)) return;

            instrument.executeAbility(
                    (ServerLevel) player.level(),
                    player,
                    stack,
                    packet.abilityIndex(),
                    target
            );
        });
    }
}
