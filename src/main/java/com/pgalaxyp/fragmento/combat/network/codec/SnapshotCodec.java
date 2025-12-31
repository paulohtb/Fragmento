package com.pgalaxyp.fragmento.combat.network.codec;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.combat.state.snapshot.SkillSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.WeaponSnapshot;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

public final class SnapshotCodec {

    public static CombatSnapshotPayload decode(FriendlyByteBuf buf) {
        CombatSnapshotVersion version = new CombatSnapshotVersion(buf.readVarLong());

        int comboIndex = buf.readVarInt();
        WeaponSnapshot weapon = new WeaponSnapshot(comboIndex);

        int cooldownCount = buf.readVarInt();
        Map<SkillId, CombatTime> cooldowns = new HashMap<>();
        for (int i = 0; i < cooldownCount; i++) {
            SkillId skillId = new SkillId(buf.readVarInt());
            CombatTime endsAt = CombatTime.ofTicks(buf.readVarLong());
            cooldowns.put(skillId, endsAt);
        }

        SkillSnapshot skills = new SkillSnapshot(cooldowns);

        CombatSnapshot snapshot = new CombatSnapshot(
                version,
                weapon,
                skills
        );

        return new CombatSnapshotPayload(snapshot);
    }

    public static void encode(CombatSnapshotPayload msg, FriendlyByteBuf buf) {
        CombatSnapshot snap = msg.snapshot();

        buf.writeVarLong(snap.version().value());

        WeaponSnapshot weapon = snap.weapon();
        buf.writeVarInt(weapon.comboIndex());

        SkillSnapshot skills = snap.skills();
        buf.writeVarInt(skills.cooldownEndsAt().size());
        for (var entry : skills.cooldownEndsAt().entrySet()) {
            buf.writeVarInt(entry.getKey().value());
            buf.writeVarLong(entry.getValue().ticks());
        }
    }

    private SnapshotCodec() {}
}