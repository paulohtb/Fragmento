package com.pgalaxyp.fragmento.content.bard.entity;

import java.util.UUID;
import net.minecraft.world.phys.Vec3;

public final class BardSkillState {

    public enum CastState {
        CHANNELING,
        CASTED,
        DESPAWNING
    }

    private CastState castState = CastState.CHANNELING;

    private UUID ownerUuid;
    private int targetEntityId;
    private UUID sourceInstrumentUuid;

    private Vec3 castAnchorPos;

    private int despawnTicksRemaining;

    public CastState castState() {
        return castState;
    }

    public void setCastState(CastState state) {
        this.castState = state;
    }

    public UUID ownerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public int targetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(int targetEntityId) {
        this.targetEntityId = Math.max(0, targetEntityId);
    }

    public UUID sourceInstrumentUuid() {
        return sourceInstrumentUuid;
    }

    public void setSourceInstrumentUuid(UUID sourceInstrumentUuid) {
        this.sourceInstrumentUuid = sourceInstrumentUuid;
    }

    public Vec3 castAnchorPos() {
        return castAnchorPos;
    }

    public void setCastAnchorPos(Vec3 castAnchorPos) {
        this.castAnchorPos = castAnchorPos;
    }

    public int despawnTicksRemaining() {
        return despawnTicksRemaining;
    }

    public void startDespawn(int ticks) {
        despawnTicksRemaining = Math.max(0, ticks);
        castState = CastState.DESPAWNING;
    }

    public boolean tickDespawn() {
        if (despawnTicksRemaining <= 0) {
            return true;
        }
        despawnTicksRemaining--;
        return despawnTicksRemaining <= 0;
    }
}
