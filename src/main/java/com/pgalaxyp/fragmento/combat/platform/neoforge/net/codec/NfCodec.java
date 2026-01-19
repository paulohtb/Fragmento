package com.pgalaxyp.fragmento.combat.platform.neoforge.net.codec;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.ability.model.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public final class NfCodec {

    private static final int VERSION = 8;
    private static final int MSG_INTENT = 1;
    private static final int MSG_SNAPSHOT = 2;

    private static final int INTENT_JOIN = 1;
    private static final int INTENT_PERFORM = 2;

    public static byte[] encodeIntent(IntentEnvelope env) {
        if (env == null) throw new IllegalArgumentException();
        try (var outBytes = new ByteArrayOutputStream(); var out = new DataOutputStream(outBytes)) {
            out.writeInt(VERSION);
            out.writeInt(MSG_INTENT);
            writeUuid(out, env.actorId().uuid());
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
            if (ver != 5 && ver != 6 && ver != 7 && ver != VERSION) throw new IllegalArgumentException();
            if (in.readInt() != MSG_INTENT) throw new IllegalArgumentException();
            ActorId actorId = new ActorId(readUuid(in));
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
                writeActorId(out, e.getKey());
                writeActorState(out, e.getValue());
            }

            out.writeInt(snapshot.activeAbilities().size());
            for (AbilityInstanceView v : snapshot.activeAbilities()) writeAbilityView(out, v);

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
            for (int i = 0; i < size; i++) actors.put(readActorId(in), readActorState(in));

            int abilities = in.readInt();
            if (abilities < 0) throw new IllegalArgumentException();
            var views = new ArrayList<AbilityInstanceView>(abilities);
            for (int i = 0; i < abilities; i++) views.add(readAbilityView(in));

            return new GameSnapshot(frame, actors, views);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private static void writeIntent(DataOutputStream out, DomainIntent intent) throws Exception {
        if (intent instanceof ActorJoinIntent) {
            out.writeInt(INTENT_JOIN);
            return;
        }
        if (intent instanceof PerformActionIntent(var input)) {
            out.writeInt(INTENT_PERFORM);
            out.writeInt(input.ordinal());
            return;
        }
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

    private static void writeAbilityView(DataOutputStream out, AbilityInstanceView v) throws Exception {
        writeString(out, v.abilityId().value());
        writeActorId(out, v.actorId());
        out.writeLong(v.startFrame());
        out.writeLong(v.endFrame());
    }

    private static AbilityInstanceView readAbilityView(DataInputStream in) throws Exception {
        AbilityId id = new AbilityId(readString(in));
        ActorId actor = readActorId(in);
        long start = in.readLong();
        long end = in.readLong();
        return new AbilityInstanceView(id, actor, start, end);
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
        if (len < 0 || len > 1_000_000) throw new IllegalArgumentException();
        byte[] bytes = in.readNBytes(len);
        if (bytes.length != len) throw new IllegalArgumentException();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private NfCodec() {}
}
