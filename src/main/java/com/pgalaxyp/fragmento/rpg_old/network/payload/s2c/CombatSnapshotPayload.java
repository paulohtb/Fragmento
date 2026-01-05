package com.pgalaxyp.fragmento.rpg_old.network.payload.s2c;

import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystId;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.ExecutionKind;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.AbilitySnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.ComboSnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.EquippedSkillsSnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.ExecutionSnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.LoadoutSnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.LockSnapshot;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record CombatSnapshotPayload(
        CombatSnapshot snapshot
) implements CustomPacketPayload {

    public static final Type<CombatSnapshotPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "combat_snapshot"));

    public static final StreamCodec<ByteBuf, CombatSnapshotPayload> STREAM_CODEC =
            StreamCodec.of(CombatSnapshotPayload::write, CombatSnapshotPayload::read);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static void write(ByteBuf buf, CombatSnapshotPayload payload) {
        CombatSnapshot snap = payload.snapshot();

        ByteBufCodecs.VAR_LONG.encode(buf, snap.version().raw());
        ByteBufCodecs.VAR_LONG.encode(buf, snap.now() != null ? snap.now().ticks() : 0L);

        ComboSnapshot combo = snap.combo();
        ByteBufCodecs.VAR_INT.encode(buf, combo.stepIndex());
        ByteBufCodecs.VAR_LONG.encode(buf, combo.nextStepAt() != null ? combo.nextStepAt().ticks() : 0L);
        ByteBufCodecs.BOOL.encode(buf, combo.holding());
        ByteBufCodecs.BOOL.encode(buf, combo.holdLatched());

        AbilitySnapshot abilities = snap.abilities();

        ByteBufCodecs.VAR_INT.encode(buf, abilities.cooldownEndsAt().size());
        for (var e : abilities.cooldownEndsAt().entrySet()) {
            ByteBufCodecs.VAR_INT.encode(buf, e.getKey().value());
            ByteBufCodecs.VAR_LONG.encode(buf, e.getValue().ticks());
        }

        ByteBufCodecs.VAR_INT.encode(buf, abilities.infusedArmed().size());
        for (var e : abilities.infusedArmed().entrySet()) {
            ByteBufCodecs.VAR_INT.encode(buf, e.getKey().index());
            ByteBufCodecs.VAR_INT.encode(buf, e.getValue().value());
        }

        ByteBufCodecs.VAR_INT.encode(buf, abilities.casting().size());
        for (var e : abilities.casting().entrySet()) {
            ByteBufCodecs.VAR_INT.encode(buf, e.getKey().index());
            ByteBufCodecs.VAR_LONG.encode(buf, e.getValue().castEndsAt().ticks());
            ByteBufCodecs.BOOL.encode(buf, e.getValue().ready());
        }

        ExecutionSnapshot exec = snap.execution() != null ? snap.execution() : ExecutionSnapshot.idle();
        ByteBufCodecs.BOOL.encode(buf, exec.active());
        ByteBufCodecs.VAR_INT.encode(buf, exec.kind() != null ? exec.kind().ordinal() : ExecutionKind.NONE.ordinal());
        ByteBufCodecs.VAR_LONG.encode(buf, exec.endsAt() != null ? exec.endsAt().ticks() : 0L);

        LockSnapshot lock = snap.lock();
        ByteBufCodecs.VAR_INT.encode(buf, lock.actionKind() != null ? lock.actionKind().ordinal() : ActionKind.NONE.ordinal());

        if (lock.skillId() != null) {
            ByteBufCodecs.BOOL.encode(buf, true);
            ByteBufCodecs.VAR_INT.encode(buf, lock.skillId().value());
        } else {
            ByteBufCodecs.BOOL.encode(buf, false);
        }

        ByteBufCodecs.VAR_LONG.encode(buf, lock.actionEndsAt() != null ? lock.actionEndsAt().ticks() : 0L);
        ByteBufCodecs.VAR_LONG.encode(buf, lock.itemSwapLockedUntil() != null ? lock.itemSwapLockedUntil().ticks() : 0L);

        LoadoutSnapshot loadout = snap.loadout();

        if (loadout.equippedCatalyst() != null) {
            ByteBufCodecs.BOOL.encode(buf, true);
            ByteBufCodecs.VAR_INT.encode(buf, loadout.equippedCatalyst().value());
        } else {
            ByteBufCodecs.BOOL.encode(buf, false);
        }

        if (loadout.family() != null) {
            ByteBufCodecs.BOOL.encode(buf, true);
            ByteBufCodecs.STRING_UTF8.encode(buf, loadout.family().value());
        } else {
            ByteBufCodecs.BOOL.encode(buf, false);
        }

        ByteBufCodecs.BOOL.encode(buf, loadout.offhandEmpty());

        EquippedSkillsSnapshot eq = snap.equippedSkills() != null ? snap.equippedSkills() : EquippedSkillsSnapshot.empty();
        ByteBufCodecs.VAR_INT.encode(buf, eq.bySlot().size());
        for (var e : eq.bySlot().entrySet()) {
            ByteBufCodecs.VAR_INT.encode(buf, e.getKey().index());
            ByteBufCodecs.VAR_INT.encode(buf, e.getValue().value());
        }
    }

    private static CombatSnapshotPayload read(ByteBuf buf) {
        CombatSnapshotVersion version =
                new CombatSnapshotVersion(ByteBufCodecs.VAR_LONG.decode(buf));

        Time now = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));

        int stepIndex = ByteBufCodecs.VAR_INT.decode(buf);
        Time nextStepAt = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
        boolean holding = ByteBufCodecs.BOOL.decode(buf);
        boolean holdLatched = ByteBufCodecs.BOOL.decode(buf);

        ComboSnapshot combo = new ComboSnapshot(stepIndex, nextStepAt, holding, holdLatched);

        int cdCount = ByteBufCodecs.VAR_INT.decode(buf);
        Map<SkillId, Time> cooldownEndsAt = new HashMap<>();
        for (int i = 0; i < cdCount; i++) {
            SkillId skill = new SkillId(ByteBufCodecs.VAR_INT.decode(buf));
            Time endsAt = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
            cooldownEndsAt.put(skill, endsAt);
        }

        int armedCount = ByteBufCodecs.VAR_INT.decode(buf);
        Map<SkillSlotId, SkillId> infusedArmed = new HashMap<>();
        for (int i = 0; i < armedCount; i++) {
            SkillSlotId slot = new SkillSlotId(ByteBufCodecs.VAR_INT.decode(buf));
            SkillId skill = new SkillId(ByteBufCodecs.VAR_INT.decode(buf));
            infusedArmed.put(slot, skill);
        }

        int castingCount = ByteBufCodecs.VAR_INT.decode(buf);
        Map<SkillSlotId, AbilitySnapshot.CastState> casting = new HashMap<>();
        for (int i = 0; i < castingCount; i++) {
            SkillSlotId slot = new SkillSlotId(ByteBufCodecs.VAR_INT.decode(buf));
            Time endsAt = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
            boolean ready = ByteBufCodecs.BOOL.decode(buf);
            casting.put(slot, new AbilitySnapshot.CastState(endsAt, ready));
        }

        AbilitySnapshot abilities = new AbilitySnapshot(cooldownEndsAt, infusedArmed, casting);

        boolean execActive = ByteBufCodecs.BOOL.decode(buf);
        int execKindOrdinal = ByteBufCodecs.VAR_INT.decode(buf);
        ExecutionKind kind = ExecutionKind.values()[Math.max(0, Math.min(execKindOrdinal, ExecutionKind.values().length - 1))];
        Time execEndsAt = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
        ExecutionSnapshot exec = new ExecutionSnapshot(execActive, kind, execEndsAt);

        int actionKindOrdinal = ByteBufCodecs.VAR_INT.decode(buf);
        ActionKind actionKind = ActionKind.values()[Math.max(0, Math.min(actionKindOrdinal, ActionKind.values().length - 1))];

        SkillId lockedSkill = null;
        if (ByteBufCodecs.BOOL.decode(buf)) {
            lockedSkill = new SkillId(ByteBufCodecs.VAR_INT.decode(buf));
        }

        Time actionEndsAt = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
        Time itemSwapLockedUntil = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));

        LockSnapshot lock = new LockSnapshot(actionKind, lockedSkill, actionEndsAt, itemSwapLockedUntil);

        CatalystId catalyst = null;
        if (ByteBufCodecs.BOOL.decode(buf)) {
            catalyst = new CatalystId(ByteBufCodecs.VAR_INT.decode(buf));
        }

        CatalystFamilyId family = null;
        if (ByteBufCodecs.BOOL.decode(buf)) {
            family = new CatalystFamilyId(ByteBufCodecs.STRING_UTF8.decode(buf));
        }

        boolean offhandEmpty = ByteBufCodecs.BOOL.decode(buf);
        LoadoutSnapshot loadout = new LoadoutSnapshot(catalyst, family, offhandEmpty);

        int eqCount = ByteBufCodecs.VAR_INT.decode(buf);
        Map<SkillSlotId, SkillId> bySlot = new HashMap<>();
        for (int i = 0; i < eqCount; i++) {
            SkillSlotId slot = new SkillSlotId(ByteBufCodecs.VAR_INT.decode(buf));
            SkillId skill = new SkillId(ByteBufCodecs.VAR_INT.decode(buf));
            bySlot.put(slot, skill);
        }
        EquippedSkillsSnapshot equippedSkills = new EquippedSkillsSnapshot(bySlot);

        return new CombatSnapshotPayload(
                new CombatSnapshot(version, now, combo, abilities, exec, lock, loadout, equippedSkills)
        );
    }
}