package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class LivingSyncBridge {

    private final ActorIds actorIds;
    private final ActorRepository actors;

    public LivingSyncBridge(ActorIds actorIds, ActorRepository actors) {
        this.actorIds = Objects.requireNonNull(actorIds);
        this.actors = Objects.requireNonNull(actors);
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent.Post event) {
        var server = event.getServer();
        var sp = server.getPlayerList().getPlayers().stream().findFirst().orElse(null);
        if (sp == null) return;

        var level = sp.serverLevel();
        syncLevel(level, sp.getBoundingBox().inflate(32.0));
    }

    private void syncLevel(ServerLevel level, AABB area) {
        for (var e : level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAlive)) {
            var actorId = actorIds.idFor(e.getUUID());

            var p = e.position();
            var pos = new Vec3(p.x, p.y, p.z);

            var bb = e.getBoundingBox();
            var bounds = new Aabb(
                    new Vec3(bb.minX, bb.minY, bb.minZ),
                    new Vec3(bb.maxX, bb.maxY, bb.maxZ)
            );

            actors.putState(actorId, new ActorState(pos, bounds, true));
        }
    }
}