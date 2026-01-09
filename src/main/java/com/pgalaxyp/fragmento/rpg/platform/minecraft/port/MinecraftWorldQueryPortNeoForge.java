package com.pgalaxyp.fragmento.rpg.platform.minecraft.port;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.query.ExternalQueryEvent;
import com.pgalaxyp.fragmento.rpg.core.event.query.TargetingQueryRequested;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.TargetingCandidate;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.TargetingQueryResolved;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.ids.MinecraftActorIds;
import com.pgalaxyp.fragmento.rpg.port.WorldQueryPort;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MinecraftWorldQueryPortNeoForge implements WorldQueryPort {

    private final MinecraftServer server;

    public MinecraftWorldQueryPortNeoForge(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        this.server = server;
    }

    @Override
    public List<DomainResolution> resolve(FrameContext frame, GameState state, RpgContent content, List<ExternalQueryEvent> queries) {
        if (frame == null || state == null || content == null || queries == null) {
            throw new IllegalArgumentException();
        }

        List<DomainResolution> out = new ArrayList<>();

        Comparator<TargetingCandidate> cmp = Comparator.comparingInt(TargetingCandidate::distanceSquared).thenComparing(TargetingCandidate::actorId);

        for (ExternalQueryEvent q : queries) {
            if (q == null) {
                throw new IllegalArgumentException();
            }
            if (!(q instanceof TargetingQueryRequested tr)) {
                continue;
            }

            LivingEntity src = findLivingByActorId(tr.sourceActorId());
            if (src == null) {
                out.add(TargetingQueryResolved.empty(tr.queryId()));
                continue;
            }

            ServerLevel level = (ServerLevel) src.level();
            double r = 16.0;
            AABB box = new AABB(src.getX() - r, src.getY() - r, src.getZ() - r, src.getX() + r, src.getY() + r, src.getZ() + r);

            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box);
            List<TargetingCandidate> candidates = new ArrayList<>();

            for (LivingEntity e : entities) {
                if (e == null) {
                    continue;
                }
                ActorId id = MinecraftActorIds.fromUuid(e.getUUID());
                if (id.equals(tr.sourceActorId())) {
                    continue;
                }
                double dx = e.getX() - src.getX();
                double dy = e.getY() - src.getY();
                double dz = e.getZ() - src.getZ();
                double ds = dx * dx + dy * dy + dz * dz;
                int distSq = ds > (double) Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) ds;
                candidates.add(new TargetingCandidate(id, distSq));
            }

            candidates.sort(cmp);

            QueryId qid = tr.queryId();
            out.add(new TargetingQueryResolved(qid, candidates));
        }

        return List.copyOf(out);
    }

    private LivingEntity findLivingByActorId(ActorId actorId) {
        if (actorId == null) {
            return null;
        }

        for (ServerLevel level : server.getAllLevels()) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(
                    -3.0E7, -2.0E4, -3.0E7,
                    3.0E7, 2.0E4, 3.0E7
            ));
            for (LivingEntity e : entities) {
                if (e == null) {
                    continue;
                }
                if (MinecraftActorIds.fromUuid(e.getUUID()).equals(actorId)) {
                    return e;
                }
            }
        }
        return null;
    }
}