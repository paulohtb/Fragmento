package com.pgalaxyp.fragmento.combat.network;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshotVersion;
import net.minecraft.network.FriendlyByteBuf;

public final class SnapshotCodec {

    public static CombatSnapshotPayload decode(FriendlyByteBuf buf) {
        long version = buf.readVarLong();
        int comboStep = buf.readVarInt();
        boolean awaitingHit = buf.readBoolean();
        int infusionSkillId = buf.readVarInt();
        return new CombatSnapshotPayload(
                new CombatSnapshot(
                        new CombatSnapshotVersion(version),
                        comboStep,
                        awaitingHit,
                        Math.max(infusionSkillId, 0)
                )
        );
    }

    public static void encode(CombatSnapshotPayload msg, FriendlyByteBuf buf) {
        CombatSnapshot snap = msg.snapshot();
        buf.writeVarLong(snap.version().value());
        buf.writeVarInt(snap.comboStepIndex());
        buf.writeBoolean(snap.awaitingHit());
        buf.writeVarInt(Math.max(0, snap.armedInfusionSkillId()));
    }

    private SnapshotCodec() {}
}