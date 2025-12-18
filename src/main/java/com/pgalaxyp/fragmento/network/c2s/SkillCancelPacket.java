package com.pgalaxyp.fragmento.network.c2s;

import com.pgalaxyp.fragmento.system.skill.SkillAction;
import com.pgalaxyp.fragmento.system.skill.SkillRouter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.server.level.ServerPlayer;

public record SkillCancelPacket(
        int slotId
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("fragmento", "skill_cancel");

    public static final Type<SkillCancelPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SkillCancelPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> buf.writeVarInt(p.slotId()),
                    buf -> new SkillCancelPacket(buf.readVarInt())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SkillCancelPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer player) {
                SkillRouter.handlePacket(
                        player,
                        SkillAction.CANCEL,
                        packet.slotId(),
                        0
                );
            }
        });
    }
}