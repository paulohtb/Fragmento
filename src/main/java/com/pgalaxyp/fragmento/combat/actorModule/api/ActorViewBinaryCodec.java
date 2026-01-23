package com.pgalaxyp.fragmento.combat.actorModule.api;

import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.util.BinaryIo;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import java.io.*;
import java.util.*;

public final class ActorViewBinaryCodec {
    public static void write(DataOutput out, ActorView view) throws IOException {
        if (out == null || view == null) throw new IllegalArgumentException();
        var actors = view.actors();
        out.writeInt(actors.size());
        for (var e : actors.entrySet()) {
            ActorId actorId = e.getKey();
            ActorState s = e.getValue();
            BinaryIo.writeUuid(out, actorId.value());
            BinaryIo.writeString(out, s.classId().value());
            var weaponOpt = s.equippedWeaponId();
            out.writeBoolean(weaponOpt.isPresent());
            if (weaponOpt.isPresent()) BinaryIo.writeString(out, weaponOpt.get().value());
            out.writeInt(s.healthHearts());
            out.writeInt(s.maxHealthHearts());
        }
    }

    public static ActorView read(DataInput in) throws IOException {
        if (in == null) throw new IllegalArgumentException();
        int n = in.readInt();
        if (n < 0 || n > 200_000) throw new IllegalArgumentException();

        NavigableMap<ActorId, ActorState> out = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            ActorId actorId = new ActorId(BinaryIo.readUuid(in));
            ClassId classId = new ClassId(BinaryIo.readString(in));
            boolean hasWeapon = in.readBoolean();
            Optional<WeaponId> weaponOpt = hasWeapon ? Optional.of(new WeaponId(BinaryIo.readString(in))) : Optional.empty();
            int health = in.readInt();
            int max = in.readInt();
            out.put(actorId, new ActorState(classId, weaponOpt, health, max));
        }
        return new ActorView(out);
    }

    private ActorViewBinaryCodec() {}
}
