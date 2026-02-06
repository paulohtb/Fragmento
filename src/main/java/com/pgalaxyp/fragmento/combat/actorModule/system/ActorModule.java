package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.port.ActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;

public final class ActorModule {
    public static FrameSystem createSnapshotSystem(ActorSnapshotPort port) { return new ActorSnapshotSystem(port); }
    private ActorModule() {}
}
