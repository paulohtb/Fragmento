package com.pgalaxyp.fragmento.network.c2s;

import com.pgalaxyp.fragmento.system.skill.SkillAction;
import com.pgalaxyp.fragmento.system.skill.SkillRouter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SkillIntentPacket(
        int slotId,
        int targetId
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("fragmento", "skill_intent");

    public static final Type<SkillIntentPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SkillIntentPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeVarInt(p.slotId());
                        buf.writeVarInt(p.targetId());
                    },
                    buf -> new SkillIntentPacket(
                            buf.readVarInt(),
                            buf.readVarInt()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SkillIntentPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                SkillRouter.handlePacket(
                        player,
                        SkillAction.START,
                        packet.slotId(),
                        packet.targetId()
                );
            }
        });
    }
}