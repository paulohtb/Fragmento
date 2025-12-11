package com.pgalaxyp.fragmento.core.network.packet;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import com.pgalaxyp.fragmento.core.network.NetworkRegistry;
import com.pgalaxyp.fragmento.features.bard_class.ability.BardAbilityServerController;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AbilityPacket(AbilityPacket.Action action, int abilityId, int targetId)
        implements CustomPacketPayload {

    public enum Action { START, TICK, FINISH, CANCEL }

    public static final ResourceLocation ID = NetworkRegistry.id("ability");
    public static final Type<AbilityPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeEnum(p.action());
                        buf.writeVarInt(p.abilityId());
                        buf.writeVarInt(p.targetId());
                    },
                    buf -> new AbilityPacket(
                            buf.readEnum(Action.class),
                            buf.readVarInt(),
                            buf.readVarInt()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AbilityPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                ModLogger.network("ability", "Received ability packet with non server player");
                return;
            }

            ModLogger.network(
                    "ability",
                    "Received packet action=" + packet.action() +
                            " abilityId=" + packet.abilityId() +
                            " targetId=" + packet.targetId()
            );

            BardAbilityServerController.handleAbilityPacket(player, packet);
        });
    }
}
