package com.pgalaxyp.fragmento.combat.abilityModule.codec;

import com.pgalaxyp.fragmento.combat.util.BinaryIo;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.io.*;
import java.util.*;

public final class AbilityViewBinaryCodec {
    public static void write(DataOutput out, AbilityViewSnapshot view) throws IOException {
        Objects.requireNonNull(out);
        Objects.requireNonNull(view);
        var active = view.active();
        out.writeInt(active.size());
        for (var s : active) {
            BinaryIo.writeString(out, s.abilityId().value());
            BinaryIo.writeUuid(out, s.actorId().value());
            out.writeLong(s.startFrame());
            out.writeLong(s.endFrameExclusive());
        }
    }

    public static AbilityViewSnapshot read(DataInput in) throws IOException {
        Objects.requireNonNull(in);
        int n = in.readInt();
        if (n < 0 || n > 200_000) throw new IllegalArgumentException();
        var list = new ArrayList<AbilitySnapshot>(n);
        for (int i = 0; i < n; i++) {
            var abilityId = new AbilityId(BinaryIo.readString(in));
            var actorId = new ActorId(BinaryIo.readUuid(in));
            long start = in.readLong();
            long endExclusive = in.readLong();
            list.add(new AbilitySnapshot(abilityId, actorId, start, endExclusive));
        }
        return new AbilityViewSnapshot(list);
    }

    private AbilityViewBinaryCodec() {}
}