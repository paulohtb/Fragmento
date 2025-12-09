package com.pgalaxyp.fragmento.features.bard_class.spirit.base;

import com.pgalaxyp.fragmento.core.controller.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

public abstract class CastedSpiritBase extends Entity {

    public interface SpiritBehavior {
        void init(CastedSpiritBase spirit);
        void tick(CastedSpiritBase spirit);
        void onHit(CastedSpiritBase spirit, LivingEntity target);
    }

    public static final EntityDataAccessor<Integer> LIFETIME =
            SynchedEntityData.defineId(CastedSpiritBase.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<String> ANIM_KEY =
            SynchedEntityData.defineId(CastedSpiritBase.class, EntityDataSerializers.STRING);

    private final List<EntityController<?>> controllers = new ArrayList<>(5);

    private int maxLifetime;
    private LivingEntity owner;
    private LivingEntity target;

    public final FlightController<CastedSpiritBase> flightController;
    public final CollisionController<CastedSpiritBase> collisionController;
    public final BounceController<CastedSpiritBase> spiritBounceController;
    public final OrientationController<CastedSpiritBase> orientationController;
    public final SpawnController<CastedSpiritBase> spawnController;

    private SpiritBehavior behavior;

    public CastedSpiritBase(EntityType<? extends CastedSpiritBase> type, Level level) {
        super(type, level);
        this.noPhysics = true;

        this.flightController = new FlightController<>(this, CastedSpiritBase::getTarget, CastedSpiritBase::getLifetime);
        this.collisionController = new CollisionController<>(this, CastedSpiritBase::getTarget);
        this.spiritBounceController = new BounceController<>(this, CastedSpiritBase::getTarget);
        this.orientationController = new OrientationController<>(this, CastedSpiritBase::getTarget);
        this.spawnController = new SpawnController<>(this, CastedSpiritBase::setTarget);

        controllers.add(spawnController);
        controllers.add(flightController);
        controllers.add(orientationController);
        controllers.add(spiritBounceController);
        controllers.add(collisionController);
    }

    public abstract SpiritBehavior createBehavior(boolean charged);

    public void summon(LivingEntity caster, LivingEntity target, ServerLevel level, boolean charged) {
        this.owner = caster;
        this.target = target;
        this.entityData.set(LIFETIME, 0);
        this.entityData.set(ANIM_KEY, "");
        this.behavior = createBehavior(charged);
        this.behavior.init(this);
        this.spawnController.initializeSpawn(caster, target, level, charged);
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public LivingEntity getTarget() {
        return target != null && target.isAlive() ? target : null;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public int getLifetime() {
        return this.entityData.get(LIFETIME);
    }

    public String getAnimKey() {
        return this.entityData.get(ANIM_KEY);
    }

    public void setAnimKey(String key) {
        this.entityData.set(ANIM_KEY, key == null ? "" : key);
    }

    public void onHit(LivingEntity target) {
        if (behavior != null) behavior.onHit(this, target);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            movement();
            return;
        }

        int age = this.entityData.get(LIFETIME) + 1;
        this.entityData.set(LIFETIME, age);

        if (behavior != null) behavior.tick(this);

        for (EntityController<?> c : controllers) {
            if (c != collisionController) c.tick();
        }

        movement();
        collisionController.tick();
    }

    private void movement() {
        Vec3 v = this.getDeltaMovement();
        if (v.lengthSqr() > 0) this.move(MoverType.SELF, v);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIFETIME, 0);
        builder.define(ANIM_KEY, "");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(LIFETIME, tag.getInt("Lifetime"));
        this.maxLifetime = tag.getInt("MaxLifetime");
        this.entityData.set(ANIM_KEY, tag.getString("AnimKey"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.entityData.get(LIFETIME));
        tag.putInt("MaxLifetime", this.maxLifetime);
        tag.putString("AnimKey", this.entityData.get(ANIM_KEY));
    }
}
