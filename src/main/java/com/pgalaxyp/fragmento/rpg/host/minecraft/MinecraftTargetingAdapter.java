package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.EntityTarget;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.VirtualTarget;
import com.pgalaxyp.fragmento.rpg.core.math.Aabb;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.targeting.TargetQuery;
import com.pgalaxyp.fragmento.rpg.platform.api.targeting.TargetResult;
import com.pgalaxyp.fragmento.rpg.platform.api.targeting.TargetingPort;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import java.util.Optional;

public final class MinecraftTargetingAdapter implements TargetingPort {

    private final ActorIds actorIds;

    public MinecraftTargetingAdapter(ActorIds actorIds) {
        this.actorIds = actorIds;
    }

    @Override
    public Optional<TargetResult> resolve(TargetQuery q) {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return Optional.empty();

        var uuid = actorIds.uuidOf(q.sourceActorId()).orElse(null);
        if (uuid == null) return Optional.empty();

        LivingEntity source = null;
        ServerLevel level = null;

        for (var l : server.getAllLevels()) {
            var e = l.getEntity(uuid);
            if (e instanceof LivingEntity le && le.isAlive()) {
                source = le;
                level = l;
                break;
            }
        }

        if (source == null || level == null) return Optional.empty();

        var origin = new net.minecraft.world.phys.Vec3(
                q.origin().x(), q.origin().y(), q.origin().z()
        );

        var end = origin.add(
                q.direction().x() * q.maxDistance(),
                q.direction().y() * q.maxDistance(),
                q.direction().z() * q.maxDistance()
        );

        AABB sweep = source.getBoundingBox()
                .expandTowards(end.subtract(origin))
                .inflate(1.0);

        LivingEntity finalSource = source;
        var hit = ProjectileUtil.getEntityHitResult(
                level,
                source,
                origin,
                end,
                sweep,
                e -> e instanceof LivingEntity le && le.isAlive() && e != finalSource,
                0.0f
        );

        if (hit == null) {
            return Optional.of(new TargetResult(
                    new VirtualTarget(
                            q.origin().add(q.direction().mul(10.0))
                    )
            ));
        }

        var pos = hit.getLocation();
        var bb = hit.getEntity().getBoundingBox();

        Target t = new EntityTarget(
                actorIds.idFor(hit.getEntity().getUUID()),
                new Vec3(pos.x, pos.y, pos.z),
                new Aabb(
                        new Vec3(bb.minX, bb.minY, bb.minZ),
                        new Vec3(bb.maxX, bb.maxY, bb.maxZ)
                )
        );

        return Optional.of(new TargetResult(t));
    }
}