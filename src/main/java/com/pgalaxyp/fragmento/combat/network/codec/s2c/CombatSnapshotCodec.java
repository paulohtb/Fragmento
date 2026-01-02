package com.pgalaxyp.fragmento.combat.network.codec.s2c;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.domain.id.CatalystId;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.state.snapshot.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.combat.state.snapshot.ComboSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.LoadoutSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.LockSnapshot;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

public final class CombatSnapshotCodec {

    public static CombatSnapshotPayload decode(FriendlyByteBuf buf) {
        CombatSnapshotVersion version = new CombatSnapshotVersion(buf.readVarLong());

        int stepIndex = buf.readVarInt();
        CombatTime nextStepAt = CombatTime.ofTicks(buf.readVarLong());
        boolean holding = buf.readBoolean();
        boolean holdLatched = buf.readBoolean();
        ComboSnapshot combo = new ComboSnapshot(stepIndex, nextStepAt, holding, holdLatched);

        int cooldownCount = buf.readVarInt();
        Map<SkillId, CombatTime> cooldowns = new HashMap<>();
        for (int i = 0; i < cooldownCount; i++) {
            SkillId skillId = new SkillId(buf.readVarInt());
            CombatTime endsAt = CombatTime.ofTicks(buf.readVarLong());
            cooldowns.put(skillId, endsAt);
        }

        int infusedCount = buf.readVarInt();
        Map<SkillSlotId, SkillId> infused = new HashMap<>();
        for (int i = 0; i < infusedCount; i++) {
            SkillSlotId slot = new SkillSlotId(buf.readVarInt());
            SkillId skillId = new SkillId(buf.readVarInt());
            infused.put(slot, skillId);
        }

        int castCount = buf.readVarInt();
        Map<SkillSlotId, AbilitySnapshot.CastState> casting = new HashMap<>();
        for (int i = 0; i < castCount; i++) {
            SkillSlotId slot = new SkillSlotId(buf.readVarInt());
            CombatTime castEndsAt = CombatTime.ofTicks(buf.readVarLong());
            boolean ready = buf.readBoolean();
            casting.put(slot, new AbilitySnapshot.CastState(castEndsAt, ready));
        }

        AbilitySnapshot abilities = new AbilitySnapshot(cooldowns, infused, casting);

        String actionKind = buf.readUtf();
        CombatTime actionEndsAt = CombatTime.ofTicks(buf.readVarLong());
        CombatTime itemSwapLockedUntil = CombatTime.ofTicks(buf.readVarLong());
        LockSnapshot lock = new LockSnapshot(actionKind, actionEndsAt, itemSwapLockedUntil);

        boolean hasCatalyst = buf.readBoolean();
        CatalystId catalystId = hasCatalyst ? new CatalystId(buf.readVarInt()) : null;

        boolean hasFamily = buf.readBoolean();
        CatalystFamilyId familyId = hasFamily ? new CatalystFamilyId(buf.readUtf()) : null;

        boolean offhandEmpty = buf.readBoolean();
        LoadoutSnapshot loadout = new LoadoutSnapshot(catalystId, familyId, offhandEmpty);

        CombatSnapshot snapshot = new CombatSnapshot(
                version,
                combo,
                abilities,
                lock,
                loadout
        );

        return new CombatSnapshotPayload(snapshot);
    }

    public static void encode(CombatSnapshotPayload msg, FriendlyByteBuf buf) {
        CombatSnapshot snap = msg.snapshot();

        buf.writeVarLong(snap.version().value());

        ComboSnapshot combo = snap.combo();
        buf.writeVarInt(combo.stepIndex());
        buf.writeVarLong(combo.nextStepAt().ticks());
        buf.writeBoolean(combo.holding());
        buf.writeBoolean(combo.holdLatched());

        AbilitySnapshot abilities = snap.abilities();

        buf.writeVarInt(abilities.cooldownEndsAt().size());
        for (var entry : abilities.cooldownEndsAt().entrySet()) {
            buf.writeVarInt(entry.getKey().value());
            buf.writeVarLong(entry.getValue().ticks());
        }

        buf.writeVarInt(abilities.infusedArmed().size());
        for (var entry : abilities.infusedArmed().entrySet()) {
            buf.writeVarInt(entry.getKey().index());
            buf.writeVarInt(entry.getValue().value());
        }

        buf.writeVarInt(abilities.casting().size());
        for (var entry : abilities.casting().entrySet()) {
            buf.writeVarInt(entry.getKey().index());
            buf.writeVarLong(entry.getValue().castEndsAt().ticks());
            buf.writeBoolean(entry.getValue().ready());
        }

        LockSnapshot lock = snap.lock();
        buf.writeUtf(lock.actionKind());
        buf.writeVarLong(lock.actionEndsAt().ticks());
        buf.writeVarLong(lock.itemSwapLockedUntil().ticks());

        LoadoutSnapshot loadout = snap.loadout();
        if (loadout.equippedCatalyst() != null) {
            buf.writeBoolean(true);
            buf.writeVarInt(loadout.equippedCatalyst().value());
        } else {
            buf.writeBoolean(false);
        }

        if (loadout.family() != null) {
            buf.writeBoolean(true);
            buf.writeUtf(loadout.family().value());
        } else {
            buf.writeBoolean(false);
        }

        buf.writeBoolean(loadout.offhandEmpty());
    }

    private CombatSnapshotCodec() {}
}