package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.world.EntityHit;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class MinecraftWorldView implements WorldView {

    private final ActorIds actorIds;

    public MinecraftWorldView(ActorIds actorIds) {
        this.actorIds = Objects.requireNonNull(actorIds);
    }

    @Override
    public Optional<EntityHit> raycastLivingEntity(long sourceActorId, Vec3 origin, Vec3 direction, double minDistance, double maxDistance) {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return Optional.empty();

        var sourceUuid = actorIds.uuidOf(sourceActorId).orElse(null);
        if (sourceUuid == null) return Optional.empty();

        LivingEntity source = null;
        ServerLevel level = null;

        for (var l : server.getAllLevels()) {
            var e = l.getEntity(sourceUuid);
            if (e instanceof LivingEntity le && le.isAlive()) {
                source = le;
                level = l;
                break;
            }
        }

        if (source == null || level == null) return Optional.empty();

        var mcOrigin = new net.minecraft.world.phys.Vec3(origin.x(), origin.y(), origin.z());
        var mcEnd = mcOrigin.add(direction.x() * maxDistance, direction.y() * maxDistance, direction.z() * maxDistance);

        AABB sweep = source.getBoundingBox().expandTowards(mcEnd.subtract(mcOrigin)).inflate(1.0);

        LivingEntity finalSource = source;
        var hit = ProjectileUtil.getEntityHitResult(
                level,
                source,
                mcOrigin,
                mcEnd,
                sweep,
                e -> e instanceof LivingEntity le && le.isAlive() && e != finalSource,
                0.0f
        );

        if (hit == null) return Optional.empty();

        var pos = hit.getLocation();
        var dist = mcOrigin.distanceTo(pos);
        if (dist < minDistance || dist > maxDistance) return Optional.empty();

        return Optional.of(new EntityHit(
                actorIds.idFor(hit.getEntity().getUUID()),
                new Vec3(pos.x, pos.y, pos.z),
                dist
        ));
    }

    @Override
    public boolean isSolidAt(Vec3 position) {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return false;

        var level = server.overworld();
        var p = BlockPos.containing(position.x(), position.y(), position.z());
        return !level.getBlockState(p).getCollisionShape(level, p).isEmpty();
    }
}