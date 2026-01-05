package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.adapter.minecraft.context.ActorContextServer;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.world.EntityHit;
import com.pgalaxyp.fragmento.rpg.platform.api.world.RaycastQuery;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;

public final class MinecraftWorldView implements WorldView {

    private final long actorId;
    private final ActorContextServer context;
    private final ActorIds actorIds;

    public MinecraftWorldView(long actorId, ActorContextServer context, ActorIds actorIds) {
        this.actorId = actorId;
        this.context = Objects.requireNonNull(context);
        this.actorIds = Objects.requireNonNull(actorIds);
    }

    @Override
    public Optional<EntityHit> raycastLivingEntity(RaycastQuery query) {
        var player = context.player(actorId).orElse(null);
        if (player == null) return Optional.empty();

        var level = player.serverLevel();

        var o = query.origin();
        var d = query.direction().normalized();

        var mcOrigin = new net.minecraft.world.phys.Vec3(o.x(), o.y(), o.z());
        var mcEnd = mcOrigin.add(
                d.x() * query.maxDistance(),
                d.y() * query.maxDistance(),
                d.z() * query.maxDistance()
        );

        var dx = mcEnd.x - mcOrigin.x;
        var dy = mcEnd.y - mcOrigin.y;
        var dz = mcEnd.z - mcOrigin.z;

        AABB sweep = player.getBoundingBox().expandTowards(dx, dy, dz).inflate(1.0);

        var hit = ProjectileUtil.getEntityHitResult(
                level,
                player,
                mcOrigin,
                mcEnd,
                sweep,
                e -> e instanceof LivingEntity le && le.isAlive() && e != player,
                0.0f
        );

        if (hit == null) return Optional.empty();

        var loc = hit.getLocation();
        var dist = mcOrigin.distanceTo(loc);

        if (dist < query.minDistance() || dist > query.maxDistance()) return Optional.empty();

        var actor = actorIds.idFor(hit.getEntity().getUUID());

        return Optional.of(new EntityHit(
                actor,
                new Vec3(loc.x, loc.y, loc.z),
                dist
        ));
    }

    @Override
    public boolean isSolidAt(Vec3 position) {
        var level = context.level(actorId).orElse(null);
        if (level == null) return false;

        var p = BlockPos.containing(position.x(), position.y(), position.z());
        return !level.getBlockState(p).getCollisionShape(level, p).isEmpty();
    }
}