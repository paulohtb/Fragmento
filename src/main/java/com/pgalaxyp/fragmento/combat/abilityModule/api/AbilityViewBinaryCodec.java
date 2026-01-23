package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.util.BinaryIo;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.io.*;
import java.util.*;

public final class AbilityViewBinaryCodec {
    public static void write(DataOutput output, AbilityViewSnapshot view) throws IOException {
        if (output == null || view == null) throw new IllegalArgumentException();
        var active = view.active();
        output.writeInt(active.size());
        for (AbilitySnapshot snapshot : active) {
            BinaryIo.writeString(output, snapshot.abilityId().value());
            BinaryIo.writeUuid(output, snapshot.actorId().value());
            output.writeLong(snapshot.startFrame());
            output.writeLong(snapshot.endFrameExclusive());
        }
    }

    public static AbilityViewSnapshot read(DataInput in) throws IOException {
        if (in == null) throw new IllegalArgumentException();
        int n = in.readInt();
        if (n < 0 || n > 200_000) throw new IllegalArgumentException();
        var list = new ArrayList<AbilitySnapshot>(n);
        for (int i = 0; i < n; i++) {
            AbilityId abilityId = new AbilityId(BinaryIo.readString(in));
            ActorId actorId = new ActorId(readUuid(in));
            long start = in.readLong();
            long endExclusive = in.readLong();
            list.add(new AbilitySnapshot(abilityId, actorId, start, endExclusive));
        }
        return new AbilityViewSnapshot(list);
    }

    private static UUID readUuid(DataInput input) throws IOException {
        return BinaryIo.readUuid(input);
    }

    private AbilityViewBinaryCodec() {}
}