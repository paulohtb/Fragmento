package com.pgalaxyp.fragmento.feature.bard.common.ability;

import com.pgalaxyp.fragmento.feature.bard.common.combat.BardWeaponProfile;
import com.pgalaxyp.fragmento.feature.bard.common.spirit.SpiritSpawnUtil;
import com.pgalaxyp.fragmento.feature.bard.common.spirit.SpiritTargetBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class SpiritAbility<S extends SpiritTargetBase> extends AbilityBase {

    private final BardWeaponProfile profile;
    private final boolean charged;
    private final double minForward;
    private final double maxForward;
    private final double maxSideOffset;
    private final int extraLifetimeTicks;
    private final Function<ServerLevel, S> spiritFactory;

    public SpiritAbility(
            BardWeaponProfile profile,
            boolean charged,
            double minForward,
            double maxForward,
            double maxSideOffset,
            int extraLifetimeTicks,
            Function<ServerLevel, S> spiritFactory
    ) {
        this.profile = profile;
        this.charged = charged;
        this.minForward = minForward;
        this.maxForward = maxForward;
        this.maxSideOffset = maxSideOffset;
        this.extraLifetimeTicks = extraLifetimeTicks;
        this.spiritFactory = spiritFactory;
        this.setRange(profile.getRange(charged));
    }

    public BardWeaponProfile getProfile() {
        return this.profile;
    }

    public boolean isCharged() {
        return this.charged;
    }

    @Override
    protected void applyToTarget(LivingEntity caster, LivingEntity target, Vec3 hitPos) {
        if (!(caster.level() instanceof ServerLevel level)) {
            return;
        }

        S spirit = this.spiritFactory.apply(level);

        Vec3 spawnPos = SpiritSpawnUtil.aroundTarget(
                caster,
                target,
                level,
                this.minForward,
                this.maxForward,
                this.maxSideOffset
        );

        int spawnTicks = this.profile.getIdleTicks(this.charged);
        int travelTicks = this.profile.getTravelTicks(this.charged);
        double collisionRadius = this.profile.getCollisionRadius(this.charged);

        spirit.setOwner(caster);
        spirit.setTarget(target);

        spirit.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        Vec3 initialTargetPos = spirit.getTargetPosition();
        spirit.faceInstantlyTowards(initialTargetPos);

        spirit.setSpawnDelay(0);

        spirit.setMoveStartAge(spawnTicks);

        spirit.configureFlight(travelTicks, collisionRadius);

        spirit.setDespawnDurationTicks(this.extraLifetimeTicks);

        int maxLifetime = spawnTicks + travelTicks + this.extraLifetimeTicks + 20;
        spirit.setMaxLifetime(maxLifetime);

        level.addFreshEntity(spirit);
    }

    @Override
    protected void applyToPosition(LivingEntity caster, Vec3 hitPos) {
    }
}
