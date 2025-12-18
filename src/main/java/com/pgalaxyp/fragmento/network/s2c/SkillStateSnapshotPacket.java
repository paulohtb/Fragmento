package com.pgalaxyp.fragmento.network.s2c;

import com.pgalaxyp.fragmento.client.data.ClientSkillState;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import com.pgalaxyp.fragmento.system.skill.SkillStateSnapshot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.EnumMap;
import java.util.UUID;

public record SkillStateSnapshotPacket(
        UUID playerId,
        EnumMap<SkillSlot, Integer> cooldowns,
        UUID instrumentId,
        int charge,
        int maxCharge
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("fragmento", "skill_state_snapshot");

    public static final Type<SkillStateSnapshotPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, SkillStateSnapshotPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeUUID(p.playerId());
                        buf.writeVarInt(p.cooldowns().size());
                        for (var e : p.cooldowns().entrySet()) {
                            buf.writeEnum(e.getKey());
                            buf.writeVarInt(e.getValue());
                        }
                        buf.writeUUID(p.instrumentId());
                        buf.writeVarInt(p.charge());
                        buf.writeVarInt(p.maxCharge());
                    },
                    buf -> {
                        UUID pid = buf.readUUID();
                        int size = buf.readVarInt();
                        EnumMap<SkillSlot, Integer> cds = new EnumMap<>(SkillSlot.class);
                        for (int i = 0; i < size; i++) {
                            SkillSlot s = buf.readEnum(SkillSlot.class);
                            int v = buf.readVarInt();
                            cds.put(s, v);
                        }
                        UUID inst = buf.readUUID();
                        int c = buf.readVarInt();
                        int m = buf.readVarInt();
                        return new SkillStateSnapshotPacket(pid, cds, inst, c, m);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public SkillStateSnapshot toSnapshot() {
        return new SkillStateSnapshot(
                playerId,
                cooldowns,
                instrumentId,
                charge,
                maxCharge
        );
    }

    public static void handle(SkillStateSnapshotPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (ctx.flow().isClientbound()) {
                ClientSkillState.update(
                        packet.cooldowns(),
                        packet.instrumentId(),
                        packet.charge(),
                        packet.maxCharge()
                );
            }
        });
    }
}