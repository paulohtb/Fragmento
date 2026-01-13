package com.pgalaxyp.fragmento.rpg.targeting.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.targeting.api.Vec3d;
import com.pgalaxyp.fragmento.rpg.targeting.api.ViewRay;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.RaycastBlockHit;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.RaycastEntityHit;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.RaycastHit;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.WorldRaycastAccess;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ProjectileUtil;

public final class NeoForgeWorldRaycast implements WorldRaycastAccess {

    private final MinecraftServer server;

    public NeoForgeWorldRaycast(MinecraftServer server) {
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

        Vec3d origin = new Vec3d(o.x, o.y, o.z);
        Vec3d dir = new Vec3d(d.x, d.y, d.z);
        return Optional.of(new ViewRay(origin, dir));
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

        ClipContext ctx = new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                caster
        );

        HitResult blockHr = level.clip(ctx);

        double maxDist = rangeBlocks;
        Vec3 blockLoc = null;

        if (blockHr != null && blockHr.getType() == HitResult.Type.BLOCK) {
            blockLoc = blockHr.getLocation();
            maxDist = start.distanceTo(blockLoc);
            if (maxDist < 0.0) {
                maxDist = 0.0;
            }
        }

        Vec3 entityEnd = start.add(dir.scale(maxDist));
        AABB box = caster.getBoundingBox().expandTowards(dir.scale(maxDist)).inflate(1.0);

        UUID casterUuid = casterId.uuid();
        Predicate<Entity> pred = e -> {
            if (e == null) {
                return false;
            }
            if (!(e instanceof LivingEntity)) {
                return false;
            }
            if (e.isSpectator()) {
                return false;
            }
            if (!e.isPickable()) {
                return false;
            }
            return !e.getUUID().equals(casterUuid);
        };

        EntityHitResult ehr = ProjectileUtil.getEntityHitResult(
                caster,
                start,
                entityEnd,
                box,
                pred,
                maxDist * maxDist
        );

        if (ehr != null && ehr.getEntity() != null) {
            Entity ent = ehr.getEntity();
            Vec3 loc = ehr.getLocation();
            double dist = start.distanceTo(loc);
            ActorId targetId = new ActorId(ent.getUUID());
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

        Entity e0 = server.getPlayerList().getPlayer(uuid);
        if (e0 instanceof LivingEntity le0) {
            return le0;
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