package com.pgalaxyp.fragmento.combat.actorModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.port.ActorHealthPort;

public final class ActorModule {
    public static ActorHealthPort createHealthPort() { return new ActorHealthEngine(); }
    private ActorModule() {}
}