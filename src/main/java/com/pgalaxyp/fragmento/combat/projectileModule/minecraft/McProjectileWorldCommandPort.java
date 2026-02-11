package com.pgalaxyp.fragmento.combat.projectileModule.minecraft;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.platformModule.minecraft.McLivingEntityLookup;
import com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileSpec;
import com.pgalaxyp.fragmento.combat.projectileModule.event.ProjectileSpawned;
import com.pgalaxyp.fragmento.combat.projectileModule.port.ProjectileWorldCommandPort;
import com.pgalaxyp.fragmento.combat.targetingModule.api.ActorTarget;
import com.pgalaxyp.fragmento.combat.targetingModule.api.PointTarget;
import com.pgalaxyp.fragmento.combat.util.Vec3d;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public final class McProjectileWorldCommandPort implements ProjectileWorldCommandPort {
    private final MinecraftServer server;
    private final Map<com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileId, ProjectileSpec> specs;

    public McProjectileWorldCommandPort(MinecraftServer server, Map<com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileId, ProjectileSpec> specs) {
        this.server = Objects.requireNonNull(server);
        this.specs = Map.copyOf(Objects.requireNonNull(specs));
    }

    @Override public void apply(ProjectileSpawned projectile) {
        Objects.requireNonNull(projectile);

        var spec = specs.get(projectile.projectileId());
        if (spec == null) return;

        LivingEntity caster = McLivingEntityLookup.findLiving(server, projectile.sourceActorId().value());
        if (caster == null) return;

        if (!(caster.level() instanceof ServerLevel level)) return;

        var key = ResourceLocation.tryParse(spec.entityTypeId());
        if (key == null) return;

        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(key);
        if (type == null) return;

        Entity created = type.create(level);
        if (created == null) return;

        Vec3 start = caster.getEyePosition(1.0f);
        Vec3 dest = targetPoint(level, projectile, caster);
        Vec3 dir = dest.subtract(start);
        if (!(Double.isFinite(dir.x) && Double.isFinite(dir.y) && Double.isFinite(dir.z)) || dir.lengthSqr() <= 0.0000001) dir = caster.getLookAngle();

        Vec3 vel = dir.normalize().scale(spec.speed());
        vel = applyInaccuracy(vel, caster.getRandom(), spec.inaccuracy());

        created.setPos(start.x, start.y, start.z);
        created.setDeltaMovement(vel);
        if (!spec.gravity()) created.setNoGravity(true);

        if (created instanceof Projectile p) p.setOwner(caster);
        if (created instanceof ThrowableItemProjectile tip) tip.setItem(new ItemStack(Items.SNOWBALL));

        created.setCustomName(Component.empty());
        level.addFreshEntity(created);
    }

    private static Vec3 targetPoint(ServerLevel level, ProjectileSpawned projectile, LivingEntity caster) {
        var t = projectile.target();
        if (t instanceof ActorTarget(ActorId actorId)) {
            LivingEntity target = McLivingEntityLookup.findLiving(level.getServer(), actorId.value());
            if (target != null) return target.getEyePosition(1.0f);
            return caster.getEyePosition(1.0f).add(caster.getLookAngle().scale(10.0));
        }
        if (t instanceof PointTarget(Vec3d p)) {
            return new Vec3(p.x(), p.y(), p.z());
        }
        return caster.getEyePosition(1.0f).add(caster.getLookAngle().scale(10.0));
    }

    private static Vec3 applyInaccuracy(Vec3 v, RandomSource r, float inaccuracy) {
        if (!(inaccuracy > 0.0f)) return v;
        double k = inaccuracy;
        return new Vec3(
                v.x + r.nextGaussian() * k,
                v.y + r.nextGaussian() * k,
                v.z + r.nextGaussian() * k
        );
    }
}