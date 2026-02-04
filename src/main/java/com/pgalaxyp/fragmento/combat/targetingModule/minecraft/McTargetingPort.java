package com.pgalaxyp.fragmento.combat.targetingModule.minecraft;

import com.pgalaxyp.fragmento.combat.util.*;
import com.pgalaxyp.fragmento.combat.targetingModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.platformModule.minecraft.McLivingEntityLookup;
import java.util.*;
import java.util.function.Predicate;
import net.minecraft.world.phys.*;
import net.minecraft.world.entity.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.ProjectileUtil;

public final class McTargetingPort implements TargetingPort {
    private final MinecraftServer server;

    public McTargetingPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override public Optional<ViewRay> viewRay(ActorId casterId) {
        LivingEntity caster = McLivingEntityLookup.findLiving(server, Objects.requireNonNull(casterId).value());
        if (caster == null) return Optional.empty();
        var o = caster.getEyePosition(1.0f);
        var d = caster.getViewVector(1.0f);
        return Optional.of(new ViewRay(new Vec3d(o.x, o.y, o.z), new Vec3d(d.x, d.y, d.z)));
    }

    @Override public Optional<RaycastHit> raycastFirstHit(ActorId casterId, ViewRay ray, double rangeBlocks) {
        if (!Double.isFinite(rangeBlocks) || rangeBlocks <= 0.0) throw new IllegalArgumentException();
        Objects.requireNonNull(ray);
        UUID casterUuid = Objects.requireNonNull(casterId).value();
        LivingEntity caster = McLivingEntityLookup.findLiving(server, casterUuid);
        if (caster == null) return Optional.empty();
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
        Predicate<Entity> pred = e -> e instanceof LivingEntity && !e.isSpectator() && e.isPickable() && !casterUuid.equals(e.getUUID());
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(caster, start, entityEnd, box, pred, maxDist * maxDist);
        if (entityHit != null) {
            Vec3 loc = entityHit.getLocation();
            return Optional.of(new RaycastEntityHit(new ActorId(entityHit.getEntity().getUUID()), new Vec3d(loc.x, loc.y, loc.z), start.distanceTo(loc)));
        }
        if (blockLoc != null) {
            return Optional.of(new RaycastBlockHit(new Vec3d(blockLoc.x, blockLoc.y, blockLoc.z), start.distanceTo(blockLoc)));
        }
        return Optional.empty();
    }
}