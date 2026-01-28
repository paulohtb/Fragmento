package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.io.*;

public final class GameSnapshotBinaryCodec {
    public static void write(DataOutput out, GameSnapshot snap) throws IOException {
        if (out == null || snap == null) throw new IllegalArgumentException();
        out.writeLong(snap.frame().frameId());
        out.writeInt(snap.frame().tickIndex());
        ActorViewBinaryCodec.write(out, snap.actors());
        AbilityViewBinaryCodec.write(out, snap.abilities());
    }

    public static GameSnapshot read(DataInput in) throws IOException {
        if (in == null) throw new IllegalArgumentException();
        FrameContext frame = new FrameContext(in.readLong(), in.readInt());
        ActorView actors = ActorViewBinaryCodec.read(in);
        AbilityViewSnapshot abilities = AbilityViewBinaryCodec.read(in);
        return new GameSnapshot(frame, actors, abilities);
    }

    private GameSnapshotBinaryCodec() {}
}