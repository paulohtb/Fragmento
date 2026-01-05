package com.pgalaxyp.fragmento.rpg.content.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class InfusedStrikeEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_AGE =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> DATA_PHASE =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> DATA_PHASE_TICKS =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> DATA_IMPACT_X =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_IMPACT_Y =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_IMPACT_Z =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DATA_HIT_DAMAGE =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DATA_VEL_Y =
            SynchedEntityData.defineId(InfusedStrikeEntity.class, EntityDataSerializers.FLOAT);

    private static final int MAX_AGE_TICKS = 26;

    private static final float DESCEND_SPEED = 1.65f;

    private static final int IMPACT_TICKS = 3;
    private static final int PULL_TICKS = 14;
    private static final int RELEASE_TICKS = 1;

    private static final double IMPACT_AOE_RADIUS = 2.35;
    private static final double VORTEX_RADIUS = 4.35;
    private static final double VORTEX_HALF_HEIGHT = 1.05;

    private static final double PULL_BASE = 0.08;
    private static final double PULL_MAX = 0.34;

    private static final double RELEASE_UP = 1.35;
    private static final double RELEASE_OUT = 0.22;

    private UUID ownerId;
    private final Set<UUID> impacted = new HashSet<>();

    private enum Phase {
        DESCEND,
        IMPACT,
        VORTEX_PULL,
        VORTEX_RELEASE,
        DONE
    }

    public InfusedStrikeEntity(EntityType<? extends InfusedStrikeEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static UUID spawn(
            ServerLevel level,
            EntityType<InfusedStrikeEntity> type,
            UUID ownerId,
            float hitDamage,
            Vec3 start,
            Vec3 impact
    ) {
        if (level == null || type == null || start == null || impact == null) return null;

        InfusedStrikeEntity e = new InfusedStrikeEntity(type, level);
        e.ownerId = ownerId;

        e.entityData.set(DATA_AGE, 0);
        e.entityData.set(DATA_PHASE, (byte) Phase.DESCEND.ordinal());
        e.entityData.set(DATA_PHASE_TICKS, 0);

        e.entityData.set(DATA_IMPACT_X, (float) impact.x);
        e.entityData.set(DATA_IMPACT_Y, (float) impact.y);
        e.entityData.set(DATA_IMPACT_Z, (float) impact.z);

        e.entityData.set(DATA_HIT_DAMAGE, Math.max(0f, hitDamage));
        e.entityData.set(DATA_VEL_Y, -DESCEND_SPEED);

        e.setPos(start);

        boolean added = level.addFreshEntity(e);
        return added ? e.getUUID() : null;
    }

    @Override
    public void tick() {
        super.tick();

        int age = entityData.get(DATA_AGE) + 1;
        entityData.set(DATA_AGE, age);

        if (age > MAX_AGE_TICKS) {
            discard();
            return;
        }

        if (level().isClientSide) {
            return;
        }

        if (!(level() instanceof ServerLevel sl)) {
            discard();
            return;
        }

        Phase phase = phase();

        if (phase == Phase.DESCEND) {
            tickDescend(sl);
            return;
        }

        if (phase == Phase.IMPACT) {
            tickImpact();
            return;
        }

        if (phase == Phase.VORTEX_PULL) {
            tickVortexPull(sl);
            return;
        }

        if (phase == Phase.VORTEX_RELEASE) {
            tickVortexRelease(sl);
            return;
        }

        discard();
    }

    private void tickDescend(ServerLevel sl) {
        Vec3 pos = position();
        Vec3 next = pos.add(0.0, entityData.get(DATA_VEL_Y), 0.0);

        Hit hit = resolveHit(sl, pos, next);

        if (hit == null) {
            move(MoverType.SELF, next.subtract(pos));
            return;
        }

        Vec3 impact = hit.pos != null ? hit.pos : impactPos();
        setPos(impact);

        if (hit.entity instanceof LivingEntity le) {
            applyImpactDamage(sl, impact, le);
        } else {
            applyImpactAoeDamage(sl, impact);
        }

        entityData.set(DATA_IMPACT_X, (float) impact.x);
        entityData.set(DATA_IMPACT_Y, (float) impact.y);
        entityData.set(DATA_IMPACT_Z, (float) impact.z);

        setPhase(Phase.IMPACT);
    }

    private void tickImpact() {
        int t = entityData.get(DATA_PHASE_TICKS) + 1;
        entityData.set(DATA_PHASE_TICKS, t);

        if (t >= IMPACT_TICKS) {
            setPhase(Phase.VORTEX_PULL);
        }
    }

    private void tickVortexPull(ServerLevel sl) {
        int t = entityData.get(DATA_PHASE_TICKS) + 1;
        entityData.set(DATA_PHASE_TICKS, t);

        Vec3 c = impactPos();

        AABB box = new AABB(
                c.x - VORTEX_RADIUS, c.y - VORTEX_HALF_HEIGHT, c.z - VORTEX_RADIUS,
                c.x + VORTEX_RADIUS, c.y + VORTEX_HALF_HEIGHT, c.z + VORTEX_RADIUS
        );

        double pct = Math.min(1.0, (double) t / (double) PULL_TICKS);
        double force = PULL_BASE + (PULL_MAX - PULL_BASE) * pct;

        for (LivingEntity le : sl.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive)) {
            if (ownerId != null && ownerId.equals(le.getUUID())) continue;

            Vec3 p = le.position();
            Vec3 d = new Vec3(c.x - p.x, 0.0, c.z - p.z);
            double len = d.length();
            if (len < 0.001) continue;

            double falloff = 1.0 - Math.min(1.0, len / VORTEX_RADIUS);
            double f = force * (0.35 + 0.65 * falloff);

            Vec3 dir = d.normalize();
            Vec3 dm = le.getDeltaMovement();

            double nx = dm.x * 0.72 + dir.x * f;
            double nz = dm.z * 0.72 + dir.z * f;
            double ny = Math.min(dm.y, 0.15) * 0.45;

            le.setDeltaMovement(nx, ny, nz);
            le.hurtMarked = true;
        }

        if (t >= PULL_TICKS) {
            setPhase(Phase.VORTEX_RELEASE);
        }
    }

    private void tickVortexRelease(ServerLevel sl) {
        Vec3 c = impactPos();

        AABB box = new AABB(
                c.x - VORTEX_RADIUS, c.y - VORTEX_HALF_HEIGHT, c.z - VORTEX_RADIUS,
                c.x + VORTEX_RADIUS, c.y + VORTEX_HALF_HEIGHT, c.z + VORTEX_RADIUS
        );

        for (LivingEntity le : sl.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive)) {
            if (ownerId != null && ownerId.equals(le.getUUID())) continue;

            Vec3 p = le.position();
            Vec3 out = new Vec3(p.x - c.x, 0.0, p.z - c.z);
            if (out.lengthSqr() < 1.0e-6) out = new Vec3(1.0, 0.0, 0.0);
            out = out.normalize().scale(RELEASE_OUT);

            le.setDeltaMovement(out.x, RELEASE_UP, out.z);
            le.hurtMarked = true;
        }

        int t = entityData.get(DATA_PHASE_TICKS) + 1;
        entityData.set(DATA_PHASE_TICKS, t);

        if (t >= RELEASE_TICKS) {
            setPhase(Phase.DONE);
            discard();
        }
    }

    private void applyImpactDamage(ServerLevel sl, Vec3 impact, LivingEntity direct) {
        float dmg = entityData.get(DATA_HIT_DAMAGE);
        if (dmg <= 0f) return;

        DamageSource src = sl.damageSources().generic();
        if (ownerId != null) {
            Entity o = sl.getEntity(ownerId);
            if (o instanceof LivingEntity lo) {
                src = sl.damageSources().mobAttack(lo);
            }
        }

        if (direct != null && direct.isAlive() && !impacted.contains(direct.getUUID())) {
            impacted.add(direct.getUUID());
            direct.hurt(src, dmg);
        }

        AABB box = new AABB(
                impact.x - IMPACT_AOE_RADIUS, impact.y - 1.0, impact.z - IMPACT_AOE_RADIUS,
                impact.x + IMPACT_AOE_RADIUS, impact.y + 2.2, impact.z + IMPACT_AOE_RADIUS
        );

        for (LivingEntity le : sl.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive)) {
            if (direct != null && le.getUUID().equals(direct.getUUID())) continue;
            if (ownerId != null && ownerId.equals(le.getUUID())) continue;
            if (impacted.contains(le.getUUID())) continue;

            Vec3 p = le.getBoundingBox().getCenter();
            double dx = p.x - impact.x;
            double dz = p.z - impact.z;
            double dist = Math.sqrt(dx * dx + dz * dz);
            double falloff = 1.0 - Math.min(1.0, dist / IMPACT_AOE_RADIUS);

            float dealt = (float) (dmg * (0.35 + 0.45 * falloff));
            if (dealt <= 0.01f) continue;

            impacted.add(le.getUUID());
            le.hurt(src, dealt);
        }
    }

    private void applyImpactAoeDamage(ServerLevel sl, Vec3 impact) {
        float dmg = entityData.get(DATA_HIT_DAMAGE);
        if (dmg <= 0f) return;

        DamageSource src = sl.damageSources().generic();
        if (ownerId != null) {
            Entity o = sl.getEntity(ownerId);
            if (o instanceof LivingEntity lo) {
                src = sl.damageSources().mobAttack(lo);
            }
        }

        AABB box = new AABB(
                impact.x - IMPACT_AOE_RADIUS, impact.y - 1.0, impact.z - IMPACT_AOE_RADIUS,
                impact.x + IMPACT_AOE_RADIUS, impact.y + 2.2, impact.z + IMPACT_AOE_RADIUS
        );

        for (LivingEntity le : sl.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive)) {
            if (ownerId != null && ownerId.equals(le.getUUID())) continue;
            if (impacted.contains(le.getUUID())) continue;

            Vec3 p = le.getBoundingBox().getCenter();
            double dx = p.x - impact.x;
            double dz = p.z - impact.z;
            double dist = Math.sqrt(dx * dx + dz * dz);
            double falloff = 1.0 - Math.min(1.0, dist / IMPACT_AOE_RADIUS);

            float dealt = (float) (dmg * (0.45 + 0.45 * falloff));
            if (dealt <= 0.01f) continue;

            impacted.add(le.getUUID());
            le.hurt(src, dealt);
        }
    }

    private Hit resolveHit(ServerLevel sl, Vec3 from, Vec3 to) {

        BlockHitResult bhr = sl.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double blockDist = Double.POSITIVE_INFINITY;
        Vec3 blockPos = null;

        if (bhr != null && bhr.getType() == HitResult.Type.BLOCK) {
            blockPos = bhr.getLocation();
            blockDist = blockPos.distanceTo(from);
        }

        AABB sweep = new AABB(
                Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z),
                Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z)
        ).inflate(0.45);

        LivingEntity closest = null;
        double best = blockDist;

        for (LivingEntity le : sl.getEntitiesOfClass(LivingEntity.class, sweep, LivingEntity::isAlive)) {
            if (ownerId != null && ownerId.equals(le.getUUID())) continue;

            AABB bb = le.getBoundingBox().inflate(0.25);
            if (!bb.intersects(sweep)) continue;

            Vec3 c = bb.getCenter();
            double d = c.distanceTo(from);

            if (d < best) {
                best = d;
                closest = le;
            }
        }

        if (closest != null) {
            Vec3 p = closest.getBoundingBox().getCenter();
            return new Hit(p, closest);
        }

        if (blockPos != null) {
            return new Hit(blockPos, null);
        }

        Vec3 impact = impactPos();
        if (to.y <= impact.y + 0.05) {
            return new Hit(impact, null);
        }

        return null;
    }

    private void setPhase(Phase p) {
        entityData.set(DATA_PHASE, (byte) (p != null ? p.ordinal() : 0));
        entityData.set(DATA_PHASE_TICKS, 0);
    }

    public Phase phase() {
        int i = entityData.get(DATA_PHASE);
        Phase[] v = Phase.values();
        if (i < 0 || i >= v.length) return Phase.DESCEND;
        return v[i];
    }

    public int phaseTicks() {
        return entityData.get(DATA_PHASE_TICKS);
    }

    public int ageTicks() {
        return entityData.get(DATA_AGE);
    }

    public Vec3 impactPos() {
        return new Vec3(
                entityData.get(DATA_IMPACT_X),
                entityData.get(DATA_IMPACT_Y),
                entityData.get(DATA_IMPACT_Z)
        );
    }

    public float hitDamage() {
        return entityData.get(DATA_HIT_DAMAGE);
    }

    public float velY() {
        return entityData.get(DATA_VEL_Y);
    }

    public boolean isVortexActive() {
        Phase p = phase();
        return p == Phase.VORTEX_PULL || p == Phase.VORTEX_RELEASE;
    }

    public boolean isImpactFlash() {
        return phase() == Phase.IMPACT;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder b) {
        b.define(DATA_AGE, 0);
        b.define(DATA_PHASE, (byte) Phase.DESCEND.ordinal());
        b.define(DATA_PHASE_TICKS, 0);
        b.define(DATA_IMPACT_X, 0f);
        b.define(DATA_IMPACT_Y, 0f);
        b.define(DATA_IMPACT_Z, 0f);
        b.define(DATA_HIT_DAMAGE, 0f);
        b.define(DATA_VEL_Y, -DESCEND_SPEED);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    private record Hit(Vec3 pos, LivingEntity entity) {}
}