package com.pgalaxyp.fragmento.combat.platform.neoforge.net.codec;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityFrameView;
import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.core.net.BinaryIo;
import com.pgalaxyp.fragmento.combat.core.state.ActorState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.intent.ActorJoinIntent;
import com.pgalaxyp.fragmento.combat.intent.DomainIntent;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.intent.PerformActionIntent;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.GameSnapshot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

public final class NfCodec {
    private static final int VERSION = 9;
    private static final int MSG_INTENT = 1;
    private static final int MSG_SNAPSHOT = 2;
    private static final int INTENT_JOIN = 1;
    private static final int INTENT_PERFORM = 2;

    public static byte[] encodeIntent(IntentEnvelope env) {
        if (env == null) throw new IllegalArgumentException();
        try (var outBytes = new ByteArrayOutputStream(); var out = new DataOutputStream(outBytes)) {
            out.writeInt(VERSION);
            out.writeInt(MSG_INTENT);
            BinaryIo.writeUuid(out, env.actorId().uuid());
            writeIntent(out, env.intent());
            Long hint = env.clientFrameHint();
            out.writeBoolean(hint != null);
            if (hint != null) out.writeLong(hint);
            out.flush();
            return outBytes.toByteArray();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static IntentEnvelope decodeIntent(byte[] data) {
        if (data == null) throw new IllegalArgumentException();
        try (var in = new DataInputStream(new ByteArrayInputStream(data))) {
            int ver = in.readInt();
            if (ver != 5 && ver != 6 && ver != 7 && ver != 8 && ver != VERSION) throw new IllegalArgumentException();
            if (in.readInt() != MSG_INTENT) throw new IllegalArgumentException();
            ActorId actorId = new ActorId(BinaryIo.readUuid(in));
            DomainIntent intent = readIntent(in, ver);
            Long hint = in.readBoolean() ? in.readLong() : null;
            return new IntentEnvelope(actorId, intent, hint);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] encodeSnapshot(GameSnapshot snapshot) {
        if (snapshot == null) throw new IllegalArgumentException();
        try (var outBytes = new ByteArrayOutputStream(); var out = new DataOutputStream(outBytes)) {
            out.writeInt(VERSION);
            out.writeInt(MSG_SNAPSHOT);
            writeFrame(out, snapshot.frame());

            out.writeInt(snapshot.actors().size());
            for (var e : snapshot.actors().entrySet()) {
                BinaryIo.writeUuid(out, e.getKey().uuid());
                writeActorState(out, e.getValue());
            }

            AbilityFrameView abilities = snapshot.abilities();

            out.writeInt(abilities.active().size());
            for (AbilitySnapshot v : abilities.active()) AbilityBinaryCodec.writeSnapshotFull(out, v);

            out.writeInt(abilities.execution().size());
            for (var e : abilities.execution().entrySet()) {
                BinaryIo.writeUuid(out, e.getKey().uuid());
                AbilityBinaryCodec.writeExecutionState(out, e.getValue());
            }

            out.flush();
            return outBytes.toByteArray();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static GameSnapshot decodeSnapshot(byte[] data) {
        if (data == null) throw new IllegalArgumentException();
        try (var in = new DataInputStream(new ByteArrayInputStream(data))) {
            int ver = in.readInt();
            if (ver != VERSION) throw new IllegalArgumentException();
            if (in.readInt() != MSG_SNAPSHOT) throw new IllegalArgumentException();

            FrameContext frame = readFrame(in);

            int size = in.readInt();
            if (size < 0) throw new IllegalArgumentException();
            var actors = new TreeMap<ActorId, ActorState>();
            for (int i = 0; i < size; i++) actors.put(new ActorId(BinaryIo.readUuid(in)), readActorState(in));

            int abilitiesSize = in.readInt();
            if (abilitiesSize < 0) throw new IllegalArgumentException();
            var active = new ArrayList<AbilitySnapshot>(abilitiesSize);
            for (int i = 0; i < abilitiesSize; i++) active.add(AbilityBinaryCodec.readSnapshotFull(in));

            int execSize = in.readInt();
            if (execSize < 0) throw new IllegalArgumentException();
            var exec = new TreeMap<ActorId, com.pgalaxyp.fragmento.combat.ability.api.AbilityExecutionState>();
            for (int i = 0; i < execSize; i++) {
                ActorId actorId = new ActorId(BinaryIo.readUuid(in));
                exec.put(actorId, AbilityBinaryCodec.readExecutionState(in, actorId));
            }

            return new GameSnapshot(frame, actors, new AbilityFrameView(active, exec));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private static void writeIntent(DataOutputStream out, DomainIntent intent) throws Exception {
        if (intent instanceof ActorJoinIntent) { out.writeInt(INTENT_JOIN); return; }
        if (intent instanceof PerformActionIntent(var input)) { out.writeInt(INTENT_PERFORM); out.writeInt(input.ordinal()); return; }
        throw new IllegalArgumentException();
    }

    private static DomainIntent readIntent(DataInputStream in, int ver) throws Exception {
        return switch (in.readInt()) {
            case INTENT_JOIN -> new ActorJoinIntent();
            case INTENT_PERFORM -> {
                ComboInput input;
                if (ver == 5) input = ComboInput.PRIMARY;
                else {
                    int ord = in.readInt();
                    ComboInput[] vals = ComboInput.values();
                    if (ord < 0 || ord >= vals.length) throw new IllegalArgumentException();
                    input = vals[ord];
                }
                yield new PerformActionIntent(input);
            }
            default -> throw new IllegalArgumentException();
        };
    }

    private static void writeFrame(DataOutputStream out, FrameContext frame) throws Exception {
        out.writeLong(frame.frameId());
        out.writeInt(frame.tickIndex());
    }

    private static FrameContext readFrame(DataInputStream in) throws Exception {
        return new FrameContext(in.readLong(), in.readInt());
    }

    private static void writeActorState(DataOutputStream out, ActorState st) throws Exception {
        BinaryIo.writeString(out, st.classId().value());
        out.writeBoolean(st.equippedWeaponId().isPresent());
        if (st.equippedWeaponId().isPresent()) BinaryIo.writeString(out, st.equippedWeaponId().get().value());
        out.writeInt(st.healthHearts());
        out.writeInt(st.maxHealthHearts());
    }

    private static ActorState readActorState(DataInputStream in) throws Exception {
        ClassId classId = new ClassId(BinaryIo.readString(in));
        Optional<WeaponId> weapon = in.readBoolean() ? Optional.of(new WeaponId(BinaryIo.readString(in))) : Optional.empty();
        int hp = in.readInt();
        int max = in.readInt();
        return new ActorState(classId, weapon, hp, max);
    }

    private NfCodec() {}
}