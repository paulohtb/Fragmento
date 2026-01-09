package com.pgalaxyp.fragmento.rpg.platform.minecraft.net;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.event.event.HomingMagicVisualEvent;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public final class FragmentoNetBytes {

    public static byte[] encodeIntent(long actorIdValue, int kind, String actionIdValueOrEmpty) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);
            out.writeLong(actorIdValue);
            out.writeInt(kind);
            out.writeUTF(actionIdValueOrEmpty == null ? "" : actionIdValueOrEmpty);
            out.flush();
            return bout.toByteArray();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static DecodedIntent decodeIntent(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
            long actorId = in.readLong();
            int kind = in.readInt();
            String action = in.readUTF();
            return new DecodedIntent(actorId, kind, action);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] encodeSnapshot(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);

            FrameContext frame = snapshot.frame();
            out.writeLong(frame.frameId());
            out.writeInt(frame.tickIndex());

            NavigableMap<ActorId, ActorState> actors = snapshot.actors();
            out.writeInt(actors.size());

            for (var e : actors.entrySet()) {
                ActorId actorId = e.getKey();
                ActorState s = e.getValue();

                out.writeLong(actorId.value());
                out.writeUTF(s.classId().value());

                if (s.equippedWeaponId().isPresent()) {
                    out.writeBoolean(true);
                    out.writeUTF(s.equippedWeaponId().get().value());
                } else {
                    out.writeBoolean(false);
                }

                if (s.combo().isPresent()) {
                    ComboState c = s.combo().get();
                    out.writeBoolean(true);
                    out.writeUTF(c.actionId().value());
                    out.writeUTF(c.weaponId().value());
                    out.writeInt(c.stepIndex());
                    out.writeInt(c.stepsTotal());
                    out.writeLong(c.lastStepFrameId());
                } else {
                    out.writeBoolean(false);
                }

                out.writeInt(s.healthHearts());
                out.writeInt(s.maxHealthHearts());
            }

            out.flush();
            return bout.toByteArray();
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
            long frameId = in.readLong();
            int tickIndex = in.readInt();
            FrameContext frame = new FrameContext(frameId, tickIndex);

            int size = in.readInt();
            NavigableMap<ActorId, ActorState> actors = new TreeMap<>();

            for (int i = 0; i < size; i++) {
                ActorId actorId = new ActorId(in.readLong());
                ClassId classId = new ClassId(in.readUTF());

                Optional<WeaponId> weapon;
                boolean hasWeapon = in.readBoolean();
                if (hasWeapon) {
                    weapon = Optional.of(new WeaponId(in.readUTF()));
                } else {
                    weapon = Optional.empty();
                }

                Optional<ComboState> combo;
                boolean hasCombo = in.readBoolean();
                if (hasCombo) {
                    ActionId actionId = new ActionId(in.readUTF());
                    WeaponId weaponId = new WeaponId(in.readUTF());
                    int stepIndex = in.readInt();
                    int stepsTotal = in.readInt();
                    long lastStepFrameId = in.readLong();
                    combo = Optional.of(new ComboState(actionId, weaponId, stepIndex, stepsTotal, lastStepFrameId));
                } else {
                    combo = Optional.empty();
                }

                int health = in.readInt();
                int max = in.readInt();

                ActorState s = new ActorState(classId, weapon, combo, health, max);
                actors.put(actorId, s);
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
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bout);

            List<HomingMagicVisualEvent> v = new ArrayList<>();
            for (DomainEvent e : events) {
                if (e instanceof HomingMagicVisualEvent h) {
                    v.add(h);
                }
            }

            out.writeInt(v.size());
            for (HomingMagicVisualEvent h : v) {
                out.writeLong(h.sourceActorId().value());
                out.writeLong(h.targetActorId().value());
                out.writeInt(h.lifetimeFrames());
            }

            out.flush();
            return bout.toByteArray();
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
            int size = in.readInt();
            List<DomainEvent> out = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ActorId src = new ActorId(in.readLong());
                ActorId dst = new ActorId(in.readLong());
                int life = in.readInt();
                out.add(new HomingMagicVisualEvent(src, dst, life));
            }
            return List.copyOf(out);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public record DecodedIntent(
            long actorIdValue,
            int kind,
            String actionIdValue
    ) {
        public DecodedIntent {
            if (kind < 0) {
                throw new IllegalArgumentException();
            }
            actionIdValue = actionIdValue == null ? "" : actionIdValue;
        }
    }

    private FragmentoNetBytes() {}
}