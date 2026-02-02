package com.pgalaxyp.fragmento.combat.snapshotModule.codec;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import com.pgalaxyp.fragmento.combat.actorModule.codec.ActorViewBinaryCodec;
import com.pgalaxyp.fragmento.combat.abilityModule.codec.AbilityViewBinaryCodec;
import java.io.*;
import java.util.Objects;

public final class CombatSnapshotBinaryCodec {
    public static void write(DataOutput out, CombatSnapshot snap) throws IOException {
        Objects.requireNonNull(out);
        Objects.requireNonNull(snap);
        out.writeLong(snap.frame().frameId());
        out.writeInt(snap.frame().tickIndex());
        ActorViewBinaryCodec.write(out, snap.actors());
        AbilityViewBinaryCodec.write(out, snap.abilities());
    }

    public static CombatSnapshot read(DataInput in) throws IOException {
        Objects.requireNonNull(in);
        var frame = new FrameContext(in.readLong(), in.readInt());
        ActorView actors = ActorViewBinaryCodec.read(in);
        AbilityViewSnapshot abilities = AbilityViewBinaryCodec.read(in);
        return new CombatSnapshot(frame, actors, abilities);
    }

    private CombatSnapshotBinaryCodec() {}
}