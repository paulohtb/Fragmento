package com.pgalaxyp.fragmento.combat.actorModule.codec;

import com.pgalaxyp.fragmento.combat.util.BinaryIo;
import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.io.*;
import java.util.*;

public final class ActorViewBinaryCodec {
    public static void write(DataOutput out, ActorView view) throws IOException {
        Objects.requireNonNull(out);
        Objects.requireNonNull(view);
        var actors = view.actors();
        out.writeInt(actors.size());
        for (var e : actors.entrySet()) {
            var id = e.getKey();
            var s = e.getValue();
            BinaryIo.writeUuid(out, id.value());
            BinaryIo.writeString(out, s.classId().value());
            out.writeInt(s.healthHearts());
            out.writeInt(s.maxHealthHearts());
        }
    }

    public static ActorView read(DataInput in) throws IOException {
        Objects.requireNonNull(in);
        int n = in.readInt();
        if (n < 0 || n > 200_000) throw new IllegalArgumentException();
        NavigableMap<ActorId, ActorState> out = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            var actorId = new ActorId(BinaryIo.readUuid(in));
            var classId = new ClassId(BinaryIo.readString(in));
            int health = in.readInt();
            int max = in.readInt();
            out.put(actorId, new ActorState(classId, health, max));
        }
        return new ActorView(out);
    }

    private ActorViewBinaryCodec() {}
}