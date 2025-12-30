package com.pgalaxyp.fragmento.combat.old.system.entity.host;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.pgalaxyp.fragmento.combat.old.system.entity.controller.EntityController;
import com.pgalaxyp.fragmento.foundation.MathUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.SynchedEntityData;

public abstract class SkillEntityBase extends Entity {

    protected final List<EntityController<?>> controllers = new ArrayList<>(6);

    private Vec3 prevPos;

    private int clientLerpSteps;
    private double clientLerpX;
    private double clientLerpY;
    private double clientLerpZ;
    private float clientLerpYRot;
    private float clientLerpXRot;
    private Vec3 clientLerpMotion;

    protected SkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    @Override
    public final void tick() {
        prevPos = position();

        if (level().isClientSide()) {
            applyClientLerp();
            clientTick();
            super.tick();
            return;
        }

        serverPreControllers();

        for (EntityController<?> controller : controllers) {
            controller.tick();
            if (isRemoved()) return;
        }

        serverPostControllers();
        if (isRemoved()) return;

        Vec3 vel = getDeltaMovement();
        if (vel.lengthSqr() > 0.000000000001) {
            move(MoverType.SELF, vel);
        }

        setDeltaMovement(Vec3.ZERO);
        super.tick();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        if (!level().isClientSide()) {
            super.lerpTo(x, y, z, yRot, xRot, steps);
            return;
        }

        if (steps <= 0) {
            clientLerpSteps = 0;
            clientLerpMotion = null;
            setPos(x, y, z);
            setYRot(yRot);
            setXRot(xRot);
            yRotO = yRot;
            xRotO = xRot;
            return;
        }

        clientLerpX = x;
        clientLerpY = y;
        clientLerpZ = z;
        clientLerpYRot = yRot;
        clientLerpXRot = xRot;
        clientLerpSteps = 1;
    }

    @Override
    public void lerpMotion(double x, double y, double z) {
        if (!level().isClientSide()) {
            super.lerpMotion(x, y, z);
            return;
        }
        clientLerpMotion = new Vec3(x, y, z);
    }

    private void applyClientLerp() {
        if (clientLerpSteps <= 0) return;

        double t = 1.0 / (double) clientLerpSteps;

        Vec3 cur = position();
        Vec3 tgt = new Vec3(clientLerpX, clientLerpY, clientLerpZ);
        Vec3 next = MathUtil.lerp(cur, tgt, t);

        setPos(next.x, next.y, next.z);

        float f = (float) t;
        float y = Mth.rotLerp(f, getYRot(), clientLerpYRot);
        float x = Mth.lerp(f, getXRot(), clientLerpXRot);

        setYRot(Mth.wrapDegrees(y));
        setXRot(Mth.wrapDegrees(x));

        yRotO = getYRot();
        xRotO = getXRot();

        if (clientLerpMotion != null) {
            setDeltaMovement(clientLerpMotion);
        } else {
            setDeltaMovement(Vec3.ZERO);
        }

        clientLerpSteps = 0;
    }

    protected void clientTick() {
    }

    protected void serverPreControllers() {
    }

    protected void serverPostControllers() {
    }

    public final Vec3 getPrevPos() {
        return prevPos != null ? prevPos : position();
    }

    public LivingEntity getOwner() {
        return null;
    }

    public UUID getOwnerUuid() {
        LivingEntity owner = getOwner();
        return owner != null ? owner.getUUID() : null;
    }

    public UUID getSourceInstrumentUuid() {
        return null;
    }

    public Vec3 resolveAnchorPosition() {
        return position();
    }

    public int summon(LivingEntity owner, LivingEntity target, ServerLevel level, com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode mode, ItemStack sourceItem) {
        if (level == null) return 0;
        if (!level.addFreshEntity(this)) return 0;
        return getId();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}