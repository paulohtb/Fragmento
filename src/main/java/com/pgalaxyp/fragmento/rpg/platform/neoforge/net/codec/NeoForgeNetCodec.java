package com.pgalaxyp.fragmento.rpg.platform.neoforge.net.codec;

import com.pgalaxyp.fragmento.rpg.ports.dto.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.domain.time.*;
import com.pgalaxyp.fragmento.rpg.core.events.event.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import java.io.*;
import java.util.*;
import java.nio.charset.*;

public final class NeoForgeNetCodec {

    private static final int VERSION = 1;

    private static final int MSG_INTENT = 1;
    private static final int MSG_SNAPSHOT = 2;
    private static final int MSG_EVENTS = 3;

    private static final int INTENT_JOIN = 1;
    private static final int INTENT_PERFORM = 2;

    private static final int EVENT_AUDIT = 1;
    private static final int EVENT_HOMING_MAGIC = 2;

    public static byte[] encodeIntent(IntentEnvelope env) {
        if (env == null) {
            throw new IllegalArgumentException();
        }
        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outBytes);

            out.writeInt(VERSION);
            out.writeInt(MSG_INTENT);

            writeUuid(out, env.actorId().uuid());
            writeIntent(out, env.intent());

            Long hint = env.clientFrameHint();
            out.writeBoolean(hint != null);
            if (hint != null) {
                out.writeLong(hint);
            }

            out.flush();
            return outBytes.toByteArray();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static IntentEnvelope decodeIntent(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

            if (in.readInt() != VERSION) {
                throw new IllegalArgumentException();
            }
            if (in.readInt() != MSG_INTENT) {
                throw new IllegalArgumentException();
            }

            ActorId actorId = new ActorId(readUuid(in));
            DomainIntent intent = readIntent(in);
            Long hint = in.readBoolean() ? in.readLong() : null;

            return new IntentEnvelope(actorId, intent, hint);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] encodeSnapshot(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
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
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static GameSnapshot decodeSnapshot(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

            if (in.readInt() != VERSION) {
                throw new IllegalArgumentException();
            }
            if (in.readInt() != MSG_SNAPSHOT) {
                throw new IllegalArgumentException();
            }

            FrameContext frame = readFrame(in);
            int size = in.readInt();
            if (size < 0) {
                throw new IllegalArgumentException();
            }

            TreeMap<ActorId, ActorState> actors = new TreeMap<>();
            for (int i = 0; i < size; i++) {
                actors.put(readActorId(in), readActorState(in));
            }

            return new GameSnapshot(frame, actors);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] encodeEvents(List<DomainEvent> events) {
        if (events == null) {
            throw new IllegalArgumentException();
        }
        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outBytes);

            out.writeInt(VERSION);
            out.writeInt(MSG_EVENTS);

            out.writeInt(events.size());
            for (DomainEvent e : events) {
                writeEvent(out, e);
            }

            out.flush();
            return outBytes.toByteArray();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static List<DomainEvent> decodeEvents(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

            if (in.readInt() != VERSION) {
                throw new IllegalArgumentException();
            }
            if (in.readInt() != MSG_EVENTS) {
                throw new IllegalArgumentException();
            }

            int size = in.readInt();
            if (size < 0) {
                throw new IllegalArgumentException();
            }

            ArrayList<DomainEvent> out = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                out.add(readEvent(in));
            }

            return List.copyOf(out);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private static void writeIntent(DataOutputStream out, DomainIntent intent) throws Exception {
        if (intent instanceof ActorJoinIntent) {
            out.writeInt(INTENT_JOIN);
        } else if (intent instanceof PerformActionIntent(var stepIndex)) {
            out.writeInt(INTENT_PERFORM);
            out.writeInt(stepIndex);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static DomainIntent readIntent(DataInputStream in) throws Exception {
        return switch (in.readInt()) {
            case INTENT_JOIN -> new ActorJoinIntent();
            case INTENT_PERFORM -> new PerformActionIntent(in.readInt());
            default -> throw new IllegalArgumentException();
        };
    }

    private static void writeEvent(DataOutputStream out, DomainEvent e) throws Exception {
        if (e instanceof AuditEvent(var message)) {
            out.writeInt(EVENT_AUDIT);
            writeString(out, message);
        } else if (e instanceof HomingMagicVisualEvent(
                long frameId,
                int localIndex,
                QueryId queryId,
                ActorId source,
                ActorId target,
                int lifetimeFrames
        )) {
            out.writeInt(EVENT_HOMING_MAGIC);
            out.writeLong(frameId);
            out.writeInt(localIndex);
            out.writeLong(queryId.value());
            writeActorId(out, source);
            writeActorId(out, target);
            out.writeInt(lifetimeFrames);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static DomainEvent readEvent(DataInputStream in) throws Exception {
        int t = in.readInt();
        if (t == EVENT_AUDIT) {
            return new AuditEvent(readString(in));
        }
        if (t == EVENT_HOMING_MAGIC) {
            return new HomingMagicVisualEvent(
                    in.readLong(),
                    in.readInt(),
                    new QueryId(in.readLong()),
                    readActorId(in),
                    readActorId(in),
                    in.readInt()
            );
        }
        throw new IllegalArgumentException();
    }

    private static void writeFrame(DataOutputStream out, FrameContext frame) throws Exception {
        out.writeLong(frame.frameId());
        out.writeInt(frame.tickIndex());
    }

    private static FrameContext readFrame(DataInputStream in) throws Exception {
        return new FrameContext(in.readLong(), in.readInt());
    }

    private static void writeActorId(DataOutputStream out, ActorId id) throws Exception {
        writeUuid(out, id.uuid());
    }

    private static ActorId readActorId(DataInputStream in) throws Exception {
        return new ActorId(readUuid(in));
    }

    private static void writeActorState(DataOutputStream out, ActorState st) throws Exception {
        writeString(out, st.classId().value());

        out.writeBoolean(st.equippedWeaponId().isPresent());
        if (st.equippedWeaponId().isPresent()) {
            writeString(out, st.equippedWeaponId().get().value());
        }

        out.writeBoolean(st.combo().isPresent());
        if (st.combo().isPresent()) {
            writeCombo(out, st.combo().get());
        }

        out.writeInt(st.healthHearts());
        out.writeInt(st.maxHealthHearts());
    }

    private static ActorState readActorState(DataInputStream in) throws Exception {
        ClassId classId = new ClassId(readString(in));

        Optional<WeaponId> weapon = in.readBoolean()
                ? Optional.of(new WeaponId(readString(in)))
                : Optional.empty();

        Optional<ComboState> combo = in.readBoolean()
                ? Optional.of(readCombo(in))
                : Optional.empty();

        return new ActorState(classId, weapon, combo, in.readInt(), in.readInt());
    }

    private static void writeCombo(DataOutputStream out, ComboState combo) throws Exception {
        writeString(out, combo.actionId().value());
        writeString(out, combo.weaponId().value());
        out.writeInt(combo.stepIndex());
        out.writeInt(combo.stepsTotal());
        out.writeLong(combo.lastStepFrameId());
    }

    private static ComboState readCombo(DataInputStream in) throws Exception {
        return new ComboState(
                new ActionId(readString(in)),
                new WeaponId(readString(in)),
                in.readInt(),
                in.readInt(),
                in.readLong()
        );
    }

    private static void writeUuid(DataOutputStream out, UUID uuid) throws Exception {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    private static UUID readUuid(DataInputStream in) throws Exception {
        return new UUID(in.readLong(), in.readLong());
    }

    private static void writeString(DataOutputStream out, String s) throws Exception {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static String readString(DataInputStream in) throws Exception {
        int len = in.readInt();
        if (len < 0 || len > 1_000_000) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = in.readNBytes(len);
        if (bytes.length != len) {
            throw new IllegalArgumentException();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private NeoForgeNetCodec() {}
}