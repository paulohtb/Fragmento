package com.pgalaxyp.fragmento.combat.transport;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.net.BinaryIo;
import com.pgalaxyp.fragmento.combat.actor.ActorState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import java.io.*;
import java.util.*;

public final class GameSnapshotBinaryCodec {
    public static void write(DataOutput out, GameSnapshot snap) throws IOException {
        if (out == null || snap == null) throw new IllegalArgumentException();

        writeFrame(out, snap.frame());
        writeActors(out, snap.actors());
        writeAbilities(out, snap.abilities());
    }

    public static GameSnapshot read(DataInput in) throws IOException {
        if (in == null) throw new IllegalArgumentException();

        FrameContext frame = readFrame(in);
        NavigableMap<ActorId, ActorState> actors = readActors(in);
        AbilityFrameView abilities = readAbilities(in);

        return new GameSnapshot(frame, actors, abilities);
    }

    private static void writeFrame(DataOutput out, FrameContext frame) throws IOException {
        out.writeLong(frame.frameId());
        out.writeInt(frame.tickIndex());
    }

    private static FrameContext readFrame(DataInput in) throws IOException {
        long frameId = in.readLong();
        int tickIndex = in.readInt();
        return new FrameContext(frameId, tickIndex);
    }

    private static void writeActors(DataOutput out, NavigableMap<ActorId, ActorState> actors) throws IOException {
        out.writeInt(actors.size());
        for (var e : actors.entrySet()) {
            ActorId actorId = e.getKey();
            ActorState s = e.getValue();

            writeActorId(out, actorId);
            writeClassId(out, s.classId());

            Optional<WeaponId> weaponOpt = s.equippedWeaponId();
            out.writeBoolean(weaponOpt.isPresent());
            if (weaponOpt.isPresent()) writeWeaponId(out, weaponOpt.get());

            out.writeInt(s.healthHearts());
            out.writeInt(s.maxHealthHearts());
        }
    }

    private static NavigableMap<ActorId, ActorState> readActors(DataInput in) throws IOException {
        int n = in.readInt();
        if (n < 0 || n > 200_000) throw new IllegalArgumentException();

        NavigableMap<ActorId, ActorState> out = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            ActorId actorId = readActorId(in);
            ClassId classId = readClassId(in);

            boolean hasWeapon = in.readBoolean();
            Optional<WeaponId> weaponOpt = Optional.empty();
            if (hasWeapon) weaponOpt = Optional.of(readWeaponId(in));

            int health = in.readInt();
            int max = in.readInt();

            out.put(actorId, new ActorState(classId, weaponOpt, health, max));
        }
        return out;
    }

    private static void writeAbilities(DataOutput out, AbilityFrameView view) throws IOException {
        var active = view.active();
        out.writeInt(active.size());
        for (AbilitySnapshot a : active) {
            writeAbilityId(out, a.abilityId());
            writeActorId(out, a.actorId());
            out.writeLong(a.startFrame());
            out.writeLong(a.endFrameExclusive());
        }
    }

    private static AbilityFrameView readAbilities(DataInput in) throws IOException {
        int n = in.readInt();
        if (n < 0 || n > 200_000) throw new IllegalArgumentException();

        var list = new ArrayList<AbilitySnapshot>(n);
        for (int i = 0; i < n; i++) {
            AbilityId abilityId = readAbilityId(in);
            ActorId actorId = readActorId(in);
            long start = in.readLong();
            long endExclusive = in.readLong();
            list.add(new AbilitySnapshot(abilityId, actorId, start, endExclusive));
        }
        return new AbilityFrameView(list);
    }

    private static void writeActorId(DataOutput out, ActorId id) throws IOException {
        BinaryIo.writeUuid(out, id.uuid());
    }

    private static ActorId readActorId(DataInput in) throws IOException {
        UUID uuid = BinaryIo.readUuid(in);
        return new ActorId(uuid);
    }

    private static void writeClassId(DataOutput out, ClassId id) throws IOException {
        BinaryIo.writeString(out, id.value());
    }

    private static ClassId readClassId(DataInput in) throws IOException {
        return new ClassId(BinaryIo.readString(in));
    }

    private static void writeWeaponId(DataOutput out, WeaponId id) throws IOException {
        BinaryIo.writeString(out, id.value());
    }

    private static WeaponId readWeaponId(DataInput in) throws IOException {
        return new WeaponId(BinaryIo.readString(in));
    }

    private static void writeAbilityId(DataOutput out, AbilityId id) throws IOException {
        BinaryIo.writeString(out, id.value());
    }

    private static AbilityId readAbilityId(DataInput in) throws IOException {
        return new AbilityId(BinaryIo.readString(in));
    }

    private GameSnapshotBinaryCodec() {}
}