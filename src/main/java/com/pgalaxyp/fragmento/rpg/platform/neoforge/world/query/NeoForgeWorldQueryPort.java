package com.pgalaxyp.fragmento.rpg.platform.neoforge.world.query;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.query.ExternalQueryEvent;
import com.pgalaxyp.fragmento.rpg.core.events.query.TargetingQueryRequested;
import com.pgalaxyp.fragmento.rpg.core.events.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.events.resolution.TargetingCandidate;
import com.pgalaxyp.fragmento.rpg.core.events.resolution.TargetingQueryResolved;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.ports.WorldQueryPort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public final class NeoForgeWorldQueryPort implements WorldQueryPort {

    private final MinecraftServer server;

    public NeoForgeWorldQueryPort(MinecraftServer server) {
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
                ActorId id = new ActorId(e.getUUID());
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

        UUID uuid = actorId.uuid();
        ServerPlayer p = server.getPlayerList().getPlayer(uuid);
        if (p != null) {
            return p;
        }

        for (ServerLevel level : server.getAllLevels()) {
            Entity e = level.getEntity(uuid);
            if (e instanceof LivingEntity le) {
                return le;
            }
        }

        return null;
    }
}