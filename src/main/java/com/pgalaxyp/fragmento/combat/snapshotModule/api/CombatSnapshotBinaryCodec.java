package com.pgalaxyp.fragmento.combat.snapshotModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.io.*;

public final class CombatSnapshotBinaryCodec {
    public static void write(DataOutput out, CombatSnapshot snap) throws IOException {
        if (out == null || snap == null) throw new IllegalArgumentException();
        out.writeLong(snap.frame().frameId());
        out.writeInt(snap.frame().tickIndex());
        ActorViewBinaryCodec.write(out, snap.actors());
        AbilityViewBinaryCodec.write(out, snap.abilities());
    }

    public static CombatSnapshot read(DataInput in) throws IOException {
        if (in == null) throw new IllegalArgumentException();
        FrameContext frame = new FrameContext(in.readLong(), in.readInt());
        ActorView actors = ActorViewBinaryCodec.read(in);
        AbilityViewSnapshot abilities = AbilityViewBinaryCodec.read(in);

        return new CombatSnapshot(frame, actors, abilities);
    }

    private CombatSnapshotBinaryCodec() {}
}