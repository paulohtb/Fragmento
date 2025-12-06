package com.pgalaxyp.fragmento.feature.bard_class.common.ability;

import com.pgalaxyp.fragmento.core.engine.AbilityBase;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritSpawner;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritTargetBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class SpiritAbility<S extends SpiritTargetBase> extends AbilityBase {

    private final boolean charged;
    private final double minForward;
    private final double maxForward;
    private final double maxSideOffset;
    private final int idleTicks;
    private final int travelTicks;
    private final double collisionRadius;
    private final int extraLifetimeTicks;
    private final Function<ServerLevel, S> spiritFactory;

    public SpiritAbility(
            boolean charged,
            double range,
            double minForward,
            double maxForward,
            double maxSideOffset,
            int idleTicks,
            int travelTicks,
            double collisionRadius,
            int extraLifetimeTicks,
            Function<ServerLevel, S> spiritFactory
    ) {
        super(range);
        this.charged = charged;
        this.minForward = minForward;
        this.maxForward = maxForward;
        this.maxSideOffset = maxSideOffset;
        this.idleTicks = idleTicks;
        this.travelTicks = travelTicks;
        this.collisionRadius = collisionRadius;
        this.extraLifetimeTicks = extraLifetimeTicks;
        this.spiritFactory = spiritFactory;
    }

    @Override
    protected void applyToTarget(LivingEntity caster, LivingEntity target, Vec3 hitPos) {
        if (!(caster.level() instanceof ServerLevel level)) {
            return;
        }

        S spirit = spiritFactory.apply(level);

        SpiritSpawner.configureAndSpawn(
                spirit,
                caster,
                target,
                level,
                minForward,
                maxForward,
                maxSideOffset,
                idleTicks,
                travelTicks,
                collisionRadius,
                extraLifetimeTicks
        );
    }
}
