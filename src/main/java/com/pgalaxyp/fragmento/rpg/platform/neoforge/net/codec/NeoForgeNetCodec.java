package com.pgalaxyp.fragmento.rpg.platform.neoforge.net.codec;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.event.AuditEvent;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.events.event.HomingMagicVisualEvent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ActorJoinIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboStartIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.TreeMap;
import java.util.UUID;

public final class NeoForgeNetCodec {

    private static final int VERSION = 1;

    private static final int MSG_INTENT = 1;
    private static final int MSG_SNAPSHOT = 2;
    private static final int MSG_EVENTS = 3;

    private static final int INTENT_JOIN = 1;
    private static final int INTENT_COMBO_START = 2;
    private static final int INTENT_COMBO_ADVANCE = 3;

    private static final int EVENT_AUDIT = 1;
    private static final int EVENT_HOMING_MAGIC = 2;

    public static byte[] encodeIntent(IntentEnvelope env) {
        if (env == null) {
            throw new IllegalArgumentException();
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeInt(VERSION);
            out.writeInt(MSG_INTENT);

            writeUuid(out, env.actorId().uuid());
            writeIntent(out, env.intent());
            writeOptionalLong(out, env.clientFrameHint());

            out.flush();
            return baos.toByteArray();
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
            int version = in.readInt();
            if (version != VERSION) {
                throw new IllegalArgumentException();
            }
            int msg = in.readInt();
            if (msg != MSG_INTENT) {
                throw new IllegalArgumentException();
            }

            ActorId actorId = new ActorId(readUuid(in));
            DomainIntent intent = readIntent(in);
            OptionalLong hint = readOptionalLong(in);

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
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeInt(VERSION);
            out.writeInt(MSG_SNAPSHOT);

            writeFrame(out, snapshot.frame());
            out.writeInt(snapshot.actors().size());

            for (var e : snapshot.actors().entrySet()) {
                ActorId actorId = e.getKey();
                ActorState state = e.getValue();
                writeActorId(out, actorId);
                writeActorState(out, state);
            }

            out.flush();
            return baos.toByteArray();
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
            int version = in.readInt();
            if (version != VERSION) {
                throw new IllegalArgumentException();
            }
            int msg = in.readInt();
            if (msg != MSG_SNAPSHOT) {
                throw new IllegalArgumentException();
            }

            FrameContext frame = readFrame(in);
            int size = in.readInt();
            if (size < 0) {
                throw new IllegalArgumentException();
            }

            TreeMap<ActorId, ActorState> actors = new TreeMap<>();
            for (int i = 0; i < size; i++) {
                ActorId actorId = readActorId(in);
                ActorState st = readActorState(in);
                actors.put(actorId, st);
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
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);
            out.writeInt(VERSION);
            out.writeInt(MSG_EVENTS);

            out.writeInt(events.size());
            for (DomainEvent e : events) {
                writeEvent(out, e);
            }

            out.flush();
            return baos.toByteArray();
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
            int version = in.readInt();
            if (version != VERSION) {
                throw new IllegalArgumentException();
            }
            int msg = in.readInt();
            if (msg != MSG_EVENTS) {
                throw new IllegalArgumentException();
            }

            int size = in.readInt();
            if (size < 0) {
                throw new IllegalArgumentException();
            }

            List<DomainEvent> out = new ArrayList<>(size);
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
            return;
        }
        if (intent instanceof ComboStartIntent) {
            out.writeInt(INTENT_COMBO_START);
            return;
        }
        if (intent instanceof ComboAdvanceIntent ca) {
            out.writeInt(INTENT_COMBO_ADVANCE);
            writeString(out, ca.actionId().value());
            return;
        }
        throw new IllegalArgumentException();
    }

    private static DomainIntent readIntent(DataInputStream in) throws Exception {
        int t = in.readInt();
        return switch (t) {
            case INTENT_JOIN -> new ActorJoinIntent();
            case INTENT_COMBO_START -> new ComboStartIntent();
            case INTENT_COMBO_ADVANCE -> new ComboAdvanceIntent(new ActionId(readString(in)));
            default -> throw new IllegalArgumentException();
        };
    }

    private static void writeEvent(DataOutputStream out, DomainEvent e) throws Exception {
        if (e instanceof AuditEvent a) {
            out.writeInt(EVENT_AUDIT);
            writeString(out, a.message());
            return;
        }
        if (e instanceof HomingMagicVisualEvent hm) {
            out.writeInt(EVENT_HOMING_MAGIC);
            out.writeLong(hm.frameId());
            out.writeInt(hm.localIndex());
            out.writeLong(hm.queryId().value());
            writeActorId(out, hm.sourceActorId());
            writeActorId(out, hm.targetActorId());
            out.writeInt(hm.lifetimeFrames());
            return;
        }
        throw new IllegalArgumentException();
    }

    private static DomainEvent readEvent(DataInputStream in) throws Exception {
        int t = in.readInt();
        if (t == EVENT_AUDIT) {
            return new AuditEvent(readString(in));
        }
        if (t == EVENT_HOMING_MAGIC) {
            long frameId = in.readLong();
            int localIndex = in.readInt();
            QueryId queryId = new QueryId(in.readLong());
            ActorId source = readActorId(in);
            ActorId target = readActorId(in);
            int lifetime = in.readInt();
            return new HomingMagicVisualEvent(frameId, localIndex, queryId, source, target, lifetime);
        }
        throw new IllegalArgumentException();
    }

    private static void writeFrame(DataOutputStream out, FrameContext frame) throws Exception {
        out.writeLong(frame.frameId());
        out.writeInt(frame.tickIndex());
    }

    private static FrameContext readFrame(DataInputStream in) throws Exception {
        long frameId = in.readLong();
        int tickIndex = in.readInt();
        return new FrameContext(frameId, tickIndex);
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

        Optional<WeaponId> weapon;
        boolean hasWeapon = in.readBoolean();
        if (hasWeapon) {
            weapon = Optional.of(new WeaponId(readString(in)));
        } else {
            weapon = Optional.empty();
        }

        Optional<ComboState> combo;
        boolean hasCombo = in.readBoolean();
        if (hasCombo) {
            combo = Optional.of(readCombo(in));
        } else {
            combo = Optional.empty();
        }

        int hp = in.readInt();
        int maxHp = in.readInt();
        return new ActorState(classId, weapon, combo, hp, maxHp);
    }

    private static void writeCombo(DataOutputStream out, ComboState combo) throws Exception {
        writeString(out, combo.actionId().value());
        writeString(out, combo.weaponId().value());
        out.writeInt(combo.stepIndex());
        out.writeInt(combo.stepsTotal());
        out.writeLong(combo.lastStepFrameId());
    }

    private static ComboState readCombo(DataInputStream in) throws Exception {
        ActionId actionId = new ActionId(readString(in));
        WeaponId weaponId = new WeaponId(readString(in));
        int stepIndex = in.readInt();
        int stepsTotal = in.readInt();
        long lastStepFrameId = in.readLong();
        return new ComboState(actionId, weaponId, stepIndex, stepsTotal, lastStepFrameId);
    }

    private static void writeOptionalLong(DataOutputStream out, OptionalLong v) throws Exception {
        out.writeBoolean(v.isPresent());
        if (v.isPresent()) {
            out.writeLong(v.getAsLong());
        }
    }

    private static OptionalLong readOptionalLong(DataInputStream in) throws Exception {
        boolean present = in.readBoolean();
        if (!present) {
            return OptionalLong.empty();
        }
        long v = in.readLong();
        return OptionalLong.of(v);
    }

    private static void writeUuid(DataOutputStream out, UUID uuid) throws Exception {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    private static UUID readUuid(DataInputStream in) throws Exception {
        long msb = in.readLong();
        long lsb = in.readLong();
        return new UUID(msb, lsb);
    }

    private static void writeString(DataOutputStream out, String s) throws Exception {
        if (s == null) {
            throw new IllegalArgumentException();
        }
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