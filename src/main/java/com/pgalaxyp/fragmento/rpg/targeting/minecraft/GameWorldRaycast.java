package com.pgalaxyp.fragmento.rpg.targeting.minecraft;

import com.pgalaxyp.fragmento.rpg.targeting.api.*;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import java.util.*;
import java.util.function.Predicate;
import net.minecraft.world.phys.*;
import net.minecraft.world.entity.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.ProjectileUtil;

public final class GameWorldRaycast implements WorldRaycastAccess {

    private final MinecraftServer server;

    public GameWorldRaycast(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        this.server = server;
    }

    @Override
    public Optional<ViewRay> viewRay(ActorId casterId) {
        if (casterId == null) {
            throw new IllegalArgumentException();
        }

        LivingEntity caster = findLivingByActorId(casterId);
        if (caster == null) {
            return Optional.empty();
        }

        Vec3 o = caster.getEyePosition(1.0f);
        Vec3 d = caster.getViewVector(1.0f);

        return Optional.of(new ViewRay(new Vec3d(o.x, o.y, o.z), new Vec3d(d.x, d.y, d.z)));
    }

    @Override
    public Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks) {
        if (casterId == null || ray == null) {
            throw new IllegalArgumentException();
        }
        if (!Double.isFinite(rangeBlocks) || rangeBlocks <= 0.0) {
            throw new IllegalArgumentException();
        }

        LivingEntity caster = findLivingByActorId(casterId);
        if (caster == null) {
            return Optional.empty();
        }

        ServerLevel level = (ServerLevel) caster.level();
        Vec3 start = new Vec3(ray.origin().x(), ray.origin().y(), ray.origin().z());
        Vec3 dir = new Vec3(ray.direction().x(), ray.direction().y(), ray.direction().z());
        Vec3 end = start.add(dir.scale(rangeBlocks));
        HitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, caster));

        double maxDist = rangeBlocks;
        Vec3 blockLoc = null;

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            blockLoc = blockHit.getLocation();
            maxDist = Math.max(0.0, start.distanceTo(blockLoc));
        }

        Vec3 entityEnd = start.add(dir.scale(maxDist));
        AABB box = caster.getBoundingBox().expandTowards(dir.scale(maxDist)).inflate(1.0);
        UUID casterUuid = casterId.uuid();
        Predicate<Entity> predicate = e -> e instanceof LivingEntity && !e.isSpectator() && e.isPickable() && !e.getUUID().equals(casterUuid);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(caster, start, entityEnd, box, predicate, maxDist * maxDist);

        if (entityHit != null) {
            Vec3 loc = entityHit.getLocation();
            double dist = start.distanceTo(loc);
            ActorId targetId = new ActorId(entityHit.getEntity().getUUID());

            return Optional.of(new RaycastEntityHit(targetId, new Vec3d(loc.x, loc.y, loc.z), dist));
        }

        if (blockLoc != null) {
            double dist = start.distanceTo(blockLoc);

            return Optional.of(new RaycastBlockHit(new Vec3d(blockLoc.x, blockLoc.y, blockLoc.z), dist));
        }

        return Optional.empty();
    }

    private LivingEntity findLivingByActorId(ActorId actorId) {
        UUID uuid = actorId.uuid();
        Entity player = server.getPlayerList().getPlayer(uuid);
        if (player instanceof LivingEntity le) {
            return le;
        }

        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(uuid);
            if (entity instanceof LivingEntity le) {
                return le;
            }
        }

        return null;
    }
}