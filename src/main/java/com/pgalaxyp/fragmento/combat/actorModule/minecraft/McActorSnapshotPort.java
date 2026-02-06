package com.pgalaxyp.fragmento.combat.actorModule.minecraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorSnapshotPort;
import java.util.*;

public final class McActorSnapshotPort implements ActorSnapshotPort {
    private final MinecraftServer server;

    public McActorSnapshotPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override public LiveActorsView snapshot() {
        var players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return LiveActorsView.empty();
        var set = new HashSet<ActorId>(players.size());
        for (ServerPlayer p : players) if (p != null) set.add(new ActorId(p.getUUID()));
        return set.isEmpty() ? LiveActorsView.empty() : new LiveActorsView(set);
    }
}