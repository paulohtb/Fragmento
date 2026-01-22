package com.pgalaxyp.fragmento.combat.client;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.actor.ActorState;
import com.pgalaxyp.fragmento.combat.transport.GameSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;

public final class DamageFeedbackRenderer {
    private final ClientSnapshotReceiver snapshots;
    private final Map<UUID, Integer> lastHeartsByActor = new HashMap<>();

    public DamageFeedbackRenderer(ClientSnapshotReceiver snapshots) {
        if (snapshots == null) throw new IllegalArgumentException();
        this.snapshots = snapshots;
    }

    public void renderTick() {
        GameSnapshot snap = snapshots.lastSnapshot();
        if (snap == null) return;

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return;

        NavigableMap<ActorId, ActorState> actors = snap.actors();

        for (var entry : actors.entrySet()) {
            ActorId actorId = entry.getKey();
            ActorState state = entry.getValue();

            UUID uuid = actorId.uuid();
            int hearts = state.healthHearts();

            Integer prev = lastHeartsByActor.put(uuid, hearts);
            if (prev == null) continue;

            if (prev > hearts) {
                Entity e = findEntity(level, uuid);
                if (e == null) continue;

                level.addParticle(
                        ParticleTypes.DAMAGE_INDICATOR,
                        e.getX(),
                        e.getEyeY(),
                        e.getZ(),
                        0.0,
                        0.1,
                        0.0
                );
            }
        }
    }

    private static Entity findEntity(ClientLevel level, UUID uuid) {
        Entity direct = level.getPlayerByUUID(uuid);
        if (direct != null) return direct;

        for (Entity e : level.entitiesForRendering()) {
            if (e != null && uuid.equals(e.getUUID())) return e;
        }
        return null;
    }
}