package com.pgalaxyp.fragmento.rpg.network.payload.s2c;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.state.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg.gameplay.state.ComboSnapshot;
import com.pgalaxyp.fragmento.rpg.gameplay.state.EffectSnapshot;
import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnSide;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CombatStateSnapshotPayload(CombatSnapshot snapshot) implements CustomPacketPayload {

    public static final Type<CombatStateSnapshotPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "combat_state_snapshot"));

    private static final StreamCodec<FriendlyByteBuf, Vec3> VEC3_CODEC =
            StreamCodec.of(
                    (buf, v) -> {
                        buf.writeDouble(v.x());
                        buf.writeDouble(v.y());
                        buf.writeDouble(v.z());
                    },
                    buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())
            );

    private static final StreamCodec<FriendlyByteBuf, EffectSnapshot> EFFECT_CODEC =
            StreamCodec.of(
                    (buf, e) -> {
                        buf.writeLong(e.effectId());
                        buf.writeUtf(e.type());
                        buf.writeUtf(e.stepId() == null ? "" : e.stepId());
                        VEC3_CODEC.encode(buf, e.position());
                        VEC3_CODEC.encode(buf, e.origin());
                        VEC3_CODEC.encode(buf, e.aim());
                    },
                    buf -> new EffectSnapshot(
                            buf.readLong(),
                            buf.readUtf(),
                            emptyToNull(buf.readUtf()),
                            VEC3_CODEC.decode(buf),
                            VEC3_CODEC.decode(buf),
                            VEC3_CODEC.decode(buf)
                    )
            );

    private static final StreamCodec<FriendlyByteBuf, ComboSnapshot> COMBO_CODEC =
            StreamCodec.of(
                    (buf, c) -> {
                        buf.writeVarInt(c.index());
                        buf.writeBoolean(c.executing());
                        buf.writeUtf(c.activeStepId() == null ? "" : c.activeStepId());
                        buf.writeEnum(c.lastSpawnSide());
                    },
                    buf -> new ComboSnapshot(
                            buf.readVarInt(),
                            buf.readBoolean(),
                            emptyToNull(buf.readUtf()),
                            buf.readEnum(SpawnSide.class)
                    )
            );

    private static final StreamCodec<FriendlyByteBuf, CombatSnapshot> SNAP_CODEC =
            StreamCodec.of(
                    (buf, s) -> {
                        buf.writeLong(s.actorId());
                        buf.writeLong(s.version());
                        COMBO_CODEC.encode(buf, s.combo());
                        buf.writeVarInt(s.effects().size());
                        for (var e : s.effects()) EFFECT_CODEC.encode(buf, e);
                    },
                    buf -> {
                        long actorId = buf.readLong();
                        long version = buf.readLong();
                        ComboSnapshot combo = COMBO_CODEC.decode(buf);
                        int n = buf.readVarInt();
                        var list = new java.util.ArrayList<EffectSnapshot>(n);
                        for (int i = 0; i < n; i++) list.add(EFFECT_CODEC.decode(buf));
                        return new CombatSnapshot(actorId, version, combo, List.copyOf(list));
                    }
            );

    public static final StreamCodec<FriendlyByteBuf, CombatStateSnapshotPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> SNAP_CODEC.encode(buf, p.snapshot()),
                    buf -> new CombatStateSnapshotPayload(SNAP_CODEC.decode(buf))
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static String emptyToNull(String s) {
        return s == null || s.isEmpty() ? null : s;
    }
}