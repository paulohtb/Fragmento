package com.pgalaxyp.fragmento.entity.bard.angel;

import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import software.bernie.geckolib.animatable.GeoEntity;
import net.minecraft.world.effect.MobEffectInstance;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import software.bernie.geckolib.animation.*;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.*;
import java.util.List;
import java.util.UUID;

public abstract class AbstractAngel extends Mob implements GeoEntity {

    protected static final String TAG_OWNER_UUID = "OwnerUUID";
    protected static final String TAG_LIFETIME_TICKS = "AngelLifeTime";

    protected static final double BUFF_RADIUS = 6.0D;
    protected static final double DEBUFF_RADIUS = 8.0D;

    protected static final int DEFAULT_LIFETIME_TICKS = 20 * 30;

    protected LivingEntity owner;
    protected Vec3 orbitOffset = Vec3.ZERO;

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    private int lifeTimeTicks = DEFAULT_LIFETIME_TICKS;

    protected AbstractAngel(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
    }

    protected AbstractAngel(EntityType<? extends Mob> type, Level level, LivingEntity owner) {
        this(type, level);
        this.owner = owner;
        applyDurationEffectToOwner();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            return;
        }

        if (!this.isAlive()) {
            return;
        }

        if (this.owner == null
                || !this.owner.isAlive()
                || this.owner.isRemoved()
                || this.owner.level() != this.level()) {

            this.discard();
            return;
        }

        if (this.lifeTimeTicks-- <= 0) {
            this.discard();
            return;
        }

        followOwner();
        tickAngelEffects();
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.lifeTimeTicks = DEFAULT_LIFETIME_TICKS;
        applyDurationEffectToOwner();
    }

    protected void applyDurationEffectToOwner() {
        if (!(this.owner instanceof Player player)) {
            return;
        }

        MobEffectInstance effect = createDurationEffect(this.lifeTimeTicks);
        if (effect != null) {
            player.addEffect(effect);
        }
    }

    protected MobEffectInstance createDurationEffect(int durationTicks) {
        return null;
    }

    protected void followOwner() {
        if (this.owner == null) {
            return;
        }

        Vec3 ownerPos = this.owner.position();
        Vec3 lookDir = this.owner.getLookAngle().normalize();

        double orbitHeight = 2.0D;
        double orbitDistance = 1.5D;

        if (this.orbitOffset.equals(Vec3.ZERO)) {
            this.orbitOffset = new Vec3(
                    (this.random.nextDouble() - 0.5D) * 0.5D,
                    0.0D,
                    (this.random.nextDouble() - 0.5D) * 0.5D
            );
        }

        Vec3 targetPos = ownerPos
                .add(-lookDir.x * orbitDistance, orbitHeight, -lookDir.z * orbitDistance)
                .add(this.orbitOffset);

        Vec3 moveVector = targetPos.subtract(this.position());
        double distance = moveVector.length();

        if (distance > 0.1D) {
            double speed = Math.min(0.15D + distance * 0.05D, 0.5D);
            Vec3 movement = moveVector.normalize().scale(speed);
            this.setDeltaMovement(movement);
            this.hurtMarked = true;
        } else {
            if (this.tickCount % 100 == 0) {
                this.orbitOffset = new Vec3(
                        (this.random.nextDouble() - 0.5D) * 0.5D,
                        (this.random.nextDouble() - 0.3D) * 0.3D,
                        (this.random.nextDouble() - 0.5D) * 0.5D
                );
            }

            float floatOffset = (float) Math.sin(this.tickCount * 0.1D) * 0.05F;
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, floatOffset, 0.0D));
        }

        this.lookAt(this.owner, 30.0F, 30.0F);
    }

    protected void tickAngelEffects() {
        applyBuffs();
        applyDebuffs();
    }

    protected void applyBuffs() {
        if (this.owner == null || !this.owner.isAlive()) {
            return;
        }

        AABB area = new AABB(
                this.owner.getX() - BUFF_RADIUS, this.owner.getY() - BUFF_RADIUS, this.owner.getZ() - BUFF_RADIUS,
                this.owner.getX() + BUFF_RADIUS, this.owner.getY() + BUFF_RADIUS, this.owner.getZ() + BUFF_RADIUS
        );

        List<Player> players = this.level().getEntitiesOfClass(Player.class, area);
        applyBuffsToPlayers(players);
    }

    protected void applyDebuffs() {
        if (this.owner == null || !this.owner.isAlive()) {
            return;
        }

        AABB area = new AABB(
                this.owner.getX() - DEBUFF_RADIUS, this.owner.getY() - DEBUFF_RADIUS, this.owner.getZ() - DEBUFF_RADIUS,
                this.owner.getX() + DEBUFF_RADIUS, this.owner.getY() + DEBUFF_RADIUS, this.owner.getZ() + DEBUFF_RADIUS
        );

        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity != this && entity != this.owner
        );
        applyDebuffsToEntities(entities);
    }

    protected void applyBuffsToPlayers(List<Player> players) {}

    protected void applyDebuffsToEntities(List<LivingEntity> entities) {}

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.hasUUID(TAG_OWNER_UUID) && this.level() instanceof ServerLevel serverLevel) {
            UUID ownerId = tag.getUUID(TAG_OWNER_UUID);
            Player playerOwner = serverLevel.getPlayerByUUID(ownerId);
            if (playerOwner != null) {
                this.owner = playerOwner;
            }
        }

        if (tag.contains(TAG_LIFETIME_TICKS)) {
            this.lifeTimeTicks = tag.getInt(TAG_LIFETIME_TICKS);
        }

        applyDurationEffectToOwner();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (this.owner instanceof Player playerOwner) {
            tag.putUUID(TAG_OWNER_UUID, playerOwner.getUUID());
        }

        tag.putInt(TAG_LIFETIME_TICKS, this.lifeTimeTicks);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }

    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(
                this,
                "idle",
                0,
                state -> PlayState.STOP
        ));
    }
}