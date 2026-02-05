package com.pgalaxyp.fragmento.combat.actorModule.minecraft;

import com.pgalaxyp.fragmento.combat.util.HealthUnits;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;

public final class McActorSnapshotPort implements ActorSnapshotPort {
    private final MinecraftServer server;
    private final ClassId defaultClassId;

    public McActorSnapshotPort(MinecraftServer server, ClassId defaultClassId) {
        this.server = Objects.requireNonNull(server);
        this.defaultClassId = Objects.requireNonNull(defaultClassId);
    }

    @Override public ActorView snapshot() {
        var players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return ActorView.empty();
        var map = new TreeMap<ActorId, ActorState>();
        for (ServerPlayer p : players) {
            if (p == null) continue;
            int hearts = HealthUnits.heartsFromHealthPoints(p.getHealth());
            int maxHearts = Math.max(1, HealthUnits.heartsFromHealthPoints(p.getMaxHealth()));
            if (hearts > maxHearts) hearts = maxHearts;
            map.put(new ActorId(p.getUUID()), new ActorState(defaultClassId, hearts, maxHearts));
        }
        return map.isEmpty() ? ActorView.empty() : new ActorView(map);
    }
}
