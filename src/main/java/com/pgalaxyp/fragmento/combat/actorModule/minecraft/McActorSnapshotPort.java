package com.pgalaxyp.fragmento.combat.actorModule.minecraft;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorObservation;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class McActorSnapshotPort implements ActorSnapshotPort {

    private final MinecraftServer server;
    private final ClassId defaultClassId;

    public McActorSnapshotPort(MinecraftServer server, ClassId defaultClassId) {
        this.server = Objects.requireNonNull(server);
        this.defaultClassId = Objects.requireNonNull(defaultClassId);
    }

    @Override
    public List<ActorObservation> snapshot() {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players == null || players.isEmpty()) return List.of();

        var out = new ArrayList<ActorObservation>(players.size());
        for (ServerPlayer p : players) {
            if (p == null) continue;

            float hp = p.getHealth();
            float maxHp = p.getMaxHealth();

            int hearts = toHearts(hp);
            int maxHearts = Math.max(1, toHearts(maxHp));

            if (hearts > maxHearts) hearts = maxHearts;

            out.add(new ActorObservation(new ActorId(p.getUUID()), defaultClassId, hearts, maxHearts));
        }
        return List.copyOf(out);
    }

    private static int toHearts(float healthPoints) {
        if (!Float.isFinite(healthPoints)) return 0;
        if (healthPoints <= 0.0f) return 0;
        double hearts = Math.ceil(healthPoints / 2.0);
        if (!Double.isFinite(hearts)) return 0;
        if (hearts > (double) Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) hearts;
    }
}