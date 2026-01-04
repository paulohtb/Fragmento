package com.pgalaxyp.fragmento.rpg.network.payload.s2c;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg.domain.id.CatalystId;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg.state.snapshot.AbilitySnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.rpg.state.snapshot.ComboSnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.LoadoutSnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.LockSnapshot;
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

        ByteBufCodecs.VAR_LONG.encode(buf, snap.version().value());

        ComboSnapshot combo = snap.combo();
        ByteBufCodecs.VAR_INT.encode(buf, combo.stepIndex());
        ByteBufCodecs.VAR_LONG.encode(buf, combo.nextStepAt().ticks());
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

        LockSnapshot lock = snap.lock();
        ActionKind kind = lock.actionKind() != null ? lock.actionKind() : ActionKind.NONE;
        ByteBufCodecs.VAR_INT.encode(buf, kind.ordinal());

        if (lock.skillId() != null) {
            ByteBufCodecs.BOOL.encode(buf, true);
            ByteBufCodecs.VAR_INT.encode(buf, lock.skillId().value());
        } else {
            ByteBufCodecs.BOOL.encode(buf, false);
        }

        ByteBufCodecs.VAR_LONG.encode(buf, lock.actionEndsAt().ticks());
        ByteBufCodecs.VAR_LONG.encode(buf, lock.itemSwapLockedUntil().ticks());

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
    }

    private static CombatSnapshotPayload read(ByteBuf buf) {
        CombatSnapshotVersion version =
                new CombatSnapshotVersion(ByteBufCodecs.VAR_LONG.decode(buf));

        int stepIndex = ByteBufCodecs.VAR_INT.decode(buf);
        Time nextStepAt = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
        boolean holding = ByteBufCodecs.BOOL.decode(buf);
        boolean holdLatched = ByteBufCodecs.BOOL.decode(buf);
        ComboSnapshot combo = new ComboSnapshot(stepIndex, nextStepAt, holding, holdLatched);

        int cdCount = ByteBufCodecs.VAR_INT.decode(buf);
        Map<SkillId, Time> cds = new HashMap<>();
        for (int i = 0; i < cdCount; i++) {
            cds.put(
                    new SkillId(ByteBufCodecs.VAR_INT.decode(buf)),
                    Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf))
            );
        }

        int infusedCount = ByteBufCodecs.VAR_INT.decode(buf);
        Map<SkillSlotId, SkillId> infused = new HashMap<>();
        for (int i = 0; i < infusedCount; i++) {
            infused.put(
                    new SkillSlotId(ByteBufCodecs.VAR_INT.decode(buf)),
                    new SkillId(ByteBufCodecs.VAR_INT.decode(buf))
            );
        }

        int castCount = ByteBufCodecs.VAR_INT.decode(buf);
        Map<SkillSlotId, AbilitySnapshot.CastState> casting = new HashMap<>();
        for (int i = 0; i < castCount; i++) {
            casting.put(
                    new SkillSlotId(ByteBufCodecs.VAR_INT.decode(buf)),
                    new AbilitySnapshot.CastState(
                            Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf)),
                            ByteBufCodecs.BOOL.decode(buf)
                    )
            );
        }

        AbilitySnapshot abilities =
                new AbilitySnapshot(Map.copyOf(cds), Map.copyOf(infused), Map.copyOf(casting));

        ActionKind kind = ActionKind.values()[ByteBufCodecs.VAR_INT.decode(buf)];

        SkillId skillId = null;
        boolean hasSkill = ByteBufCodecs.BOOL.decode(buf);
        if (hasSkill) {
            skillId = new SkillId(ByteBufCodecs.VAR_INT.decode(buf));
        }

        Time endsAt = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
        Time swapLock = Time.ofTicks(ByteBufCodecs.VAR_LONG.decode(buf));
        LockSnapshot lock = new LockSnapshot(kind, skillId, endsAt, swapLock);

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

        return new CombatSnapshotPayload(
                new CombatSnapshot(version, combo, abilities, lock, loadout)
        );
    }
}