package com.pgalaxyp.fragmento.content.bard.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class BardSkillLifecycle {

    private final BardSkillEntityBase entity;
    private final BardSkillState state;

    public BardSkillLifecycle(BardSkillEntityBase entity, BardSkillState state) {
        this.entity = entity;
        this.state = state;
    }

    public boolean isCasted() {
        return state.castState() == BardSkillState.CastState.CASTED;
    }

    public void markCasted() {
        if (state.castState() != BardSkillState.CastState.CHANNELING) return;

        state.setCastState(BardSkillState.CastState.CASTED);

        LivingEntity t = entity.getTarget();
        Vec3 anchor = t != null ? t.position() : entity.position();
        state.setCastAnchorPos(anchor);

        entity.onCastedInternal();
    }

    public void requestDespawn(int delayTicks) {
        if (state.castState() == BardSkillState.CastState.DESPAWNING) return;
        state.startDespawn(delayTicks);
        entity.onCancelledInternal();
    }

    public boolean tick(ServerLevel level) {
        if (state.castState() != BardSkillState.CastState.DESPAWNING) {
            return false;
        }
        return state.tickDespawn();
    }
}
