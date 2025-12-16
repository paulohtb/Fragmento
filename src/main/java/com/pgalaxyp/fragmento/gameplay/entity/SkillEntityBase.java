package com.pgalaxyp.fragmento.gameplay.entity;

import com.pgalaxyp.fragmento.core.controller.EntityController;
import com.pgalaxyp.fragmento.gameplay.skill.SkillMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SkillEntityBase extends Entity {

    protected final List<EntityController<?>> controllers =
            new ArrayList<>(6);

    private Vec3 prevPos;

    protected SkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    @Override
    public final void tick() {

        if (level().isClientSide()) {
            clientTick();
            super.tick();
            return;
        }

        prevPos = position();

        serverPreControllers();

        for (EntityController<?> controller : controllers) {
            controller.tick();
            if (isRemoved()) return;
        }

        serverPostControllers();

        super.tick();
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

    public void onCastedInternal() {
    }

    public void onCancelledInternal() {
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

    public int summon(
            LivingEntity owner,
            LivingEntity target,
            ServerLevel level,
            SkillMode mode,
            ItemStack sourceItem
    ) {
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
