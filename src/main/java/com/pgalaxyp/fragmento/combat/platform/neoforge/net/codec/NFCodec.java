package com.pgalaxyp.fragmento.combat.platform.neoforge.net.codec;

import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.ports.dto.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.events.event.*;
import com.pgalaxyp.fragmento.combat.core.events.intent.*;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public final class NFCodec {

    private static final int VERSION = 2;
    private static final int MSG_INTENT = 1;
    private static final int MSG_SNAPSHOT = 2;
    private static final int MSG_EVENTS = 3;
    private static final int INTENT_JOIN = 1;
    private static final int INTENT_PERFORM = 2;
    private static final int EVENT_AUDIT = 1;
    private static final int EVENT_HOMING_MAGIC = 2;

    public static byte[] encodeIntent(IntentEnvelope env) {
        if (env == null) throw new IllegalArgumentException();

        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outBytes);

            out.writeInt(VERSION);
            out.writeInt(MSG_INTENT);

            writeUuid(out, env.actorId().uuid());
            writeIntent(out, env.intent());
            Long hint = env.clientFrameHint();
            out.writeBoolean(hint != null);
            if (hint != null) out.writeLong(hint);
            out.flush();

            return outBytes.toByteArray();
        } catch (Exception e) { throw new IllegalArgumentException(); }
    }

    public static IntentEnvelope decodeIntent(byte[] data) {
        if (data == null) throw new IllegalArgumentException();

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            if (in.readInt() != VERSION) throw new IllegalArgumentException();
            if (in.readInt() != MSG_INTENT) throw new IllegalArgumentException();

            ActorId actorId = new ActorId(readUuid(in));
            DomainIntent intent = readIntent(in);
            Long hint = in.readBoolean() ? in.readLong() : null;

            return new IntentEnvelope(actorId, intent, hint);
        } catch (Exception e) { throw new IllegalArgumentException(); }
    }

    public static byte[] encodeSnapshot(GameSnapshot snapshot) {
        if (snapshot == null) throw new IllegalArgumentException();

        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outBytes);

            out.writeInt(VERSION);
            out.writeInt(MSG_SNAPSHOT);

            writeFrame(out, snapshot.frame());
            out.writeInt(snapshot.actors().size());
            for (var e : snapshot.actors().entrySet()) {
                writeActorId(out, e.getKey());
                writeActorState(out, e.getValue());
            }

            out.flush();

            return outBytes.toByteArray();
        } catch (Exception e) { throw new IllegalArgumentException(); }
    }

    public static GameSnapshot decodeSnapshot(byte[] data) {
        if (data == null) throw new IllegalArgumentException();

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

            if (in.readInt() != VERSION) throw new IllegalArgumentException();
            if (in.readInt() != MSG_SNAPSHOT) throw new IllegalArgumentException();

            FrameContext frame = readFrame(in);
            int size = in.readInt();
            if (size < 0) throw new IllegalArgumentException();

            TreeMap<ActorId, ActorState> actors = new TreeMap<>();
            for (int i = 0; i < size; i++) actors.put(readActorId(in), readActorState(in));

            return new GameSnapshot(frame, actors);
        } catch (Exception e) { throw new IllegalArgumentException(); }
    }

    public static byte[] encodeEvents(List<DomainEvent> events) {
        if (events == null) throw new IllegalArgumentException();

        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outBytes);

            out.writeInt(VERSION);
            out.writeInt(MSG_EVENTS);

            out.writeInt(events.size());
            for (DomainEvent e : events) writeEvent(out, e);

            out.flush();

            return outBytes.toByteArray();
        } catch (Exception e) { throw new IllegalArgumentException(); }
    }

    public static List<DomainEvent> decodeEvents(byte[] data) {
        if (data == null) throw new IllegalArgumentException();

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            if (in.readInt() != VERSION) throw new IllegalArgumentException();
            if (in.readInt() != MSG_EVENTS) throw new IllegalArgumentException();

            int size = in.readInt();
            if (size < 0) throw new IllegalArgumentException();

            ArrayList<DomainEvent> out = new ArrayList<>(size);
            for (int i = 0; i < size; i++) out.add(readEvent(in));

            return List.copyOf(out);
        } catch (Exception e) { throw new IllegalArgumentException(); }
    }

    private static void writeIntent(DataOutputStream out, DomainIntent intent) throws Exception {
        if (intent instanceof ActorJoinIntent) {
            out.writeInt(INTENT_JOIN);
        } else if (intent instanceof PerformActionIntent(var input)) {
            out.writeInt(INTENT_PERFORM);
            out.writeInt(input.ordinal());
        } else throw new IllegalArgumentException();
    }

    private static DomainIntent readIntent(DataInputStream in) throws Exception {
        return switch (in.readInt()) {
            case INTENT_JOIN -> new ActorJoinIntent();
            case INTENT_PERFORM -> {
                int ord = in.readInt();
                ComboInput[] vals = ComboInput.values();
                if (ord < 0 || ord >= vals.length) throw new IllegalArgumentException();
                yield new PerformActionIntent(vals[ord]);
            }
            default -> throw new IllegalArgumentException();
        };
    }

    private static void writeEvent(DataOutputStream output, DomainEvent event) throws Exception {
        if (event instanceof AuditEvent(var message)) {
            output.writeInt(EVENT_AUDIT);
            writeString(output, message);

            return;
        }
        if (event instanceof HomingMagicVisualEvent(long frameId, int localIndex, QueryId queryId, ActorId source, ActorId target, int lifetimeFrames)) {
            output.writeInt(EVENT_HOMING_MAGIC);
            output.writeLong(frameId);
            output.writeInt(localIndex);
            output.writeLong(queryId.value());
            writeActorId(output, source);
            writeActorId(output, target);
            output.writeInt(lifetimeFrames);

            return;
        } throw new IllegalArgumentException();
    }

    private static DomainEvent readEvent(DataInputStream input) throws Exception {
        int t = input.readInt();
        if (t == EVENT_AUDIT) return new AuditEvent(readString(input));
        if (t == EVENT_HOMING_MAGIC) {
            return new HomingMagicVisualEvent(input.readLong(), input.readInt(), new QueryId(input.readLong()), readActorId(input), readActorId(input), input.readInt());
        } throw new IllegalArgumentException();
    }

    private static void writeFrame(DataOutputStream out, FrameContext frame) throws Exception {
        out.writeLong(frame.frameId());
        out.writeInt(frame.tickIndex());
    }

    private static FrameContext readFrame(DataInputStream in) throws Exception { return new FrameContext(in.readLong(), in.readInt()); }
    private static void writeActorId(DataOutputStream out, ActorId id) throws Exception { writeUuid(out, id.uuid()); }
    private static ActorId readActorId(DataInputStream in) throws Exception { return new ActorId(readUuid(in)); }

    private static void writeActorState(DataOutputStream out, ActorState st) throws Exception {
        writeString(out, st.classId().value());
        out.writeBoolean(st.equippedWeaponId().isPresent());
        if (st.equippedWeaponId().isPresent()) writeString(out, st.equippedWeaponId().get().value());

        out.writeInt(st.healthHearts());
        out.writeInt(st.maxHealthHearts());
    }

    private static ActorState readActorState(DataInputStream in) throws Exception {
        ClassId classId = new ClassId(readString(in));
        Optional<WeaponId> weapon = in.readBoolean() ? Optional.of(new WeaponId(readString(in))) : Optional.empty();
        int hp = in.readInt();
        int max = in.readInt();

        return new ActorState(classId, weapon, hp, max);
    }

    private static void writeUuid(DataOutputStream out, UUID uuid) throws Exception {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    private static UUID readUuid(DataInputStream in) throws Exception { return new UUID(in.readLong(), in.readLong()); }

    private static void writeString(DataOutputStream out, String s) throws Exception {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static String readString(DataInputStream in) throws Exception {
        int len = in.readInt();
        if (len < 0 || len > 1_000_000) throw new IllegalArgumentException();
        byte[] bytes = in.readNBytes(len);
        if (bytes.length != len) throw new IllegalArgumentException();

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private NFCodec() {}
}