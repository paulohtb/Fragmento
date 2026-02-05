package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.port.ActorHealthPort;

public final class ActorModule {
    public static ActorHealthPort createHealthPort() { return new ActorHealthEngine(); }
    public static com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem createSnapshotSystem(com.pgalaxyp.fragmento.combat.actorModule.port.ActorSnapshotPort port) { return new ActorSnapshotSystem(port); }
    public static com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem createHealthFromDamageSystem(ActorHealthPort health) { return new ActorHealthFromDamageSystem(health); }
    private ActorModule() {}
}
