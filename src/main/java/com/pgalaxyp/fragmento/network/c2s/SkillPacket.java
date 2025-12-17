package com.pgalaxyp.fragmento.network.c2s;

import com.pgalaxyp.fragmento.system.skill.SkillRouter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SkillPacket(Action action, int slotId, int targetId)
        implements CustomPacketPayload {

    public enum Action {
        START,
        CANCEL
    }

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("fragmento", "skill");

    public static final Type<SkillPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SkillPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeEnum(p.action());
                        buf.writeVarInt(p.slotId());
                        buf.writeVarInt(p.targetId());
                    },
                    buf -> new SkillPacket(
                            buf.readEnum(Action.class),
                            buf.readVarInt(),
                            buf.readVarInt()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SkillPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                SkillRouter.handlePacket(player, packet);
            }
        });
    }
}
