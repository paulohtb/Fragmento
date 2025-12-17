package com.pgalaxyp.fragmento.system.skill;

import com.pgalaxyp.fragmento.system.entity.component.AreaEffectComponent;
import com.pgalaxyp.fragmento.system.entity.host.SimulatedEntity;
import com.pgalaxyp.fragmento.system.entity.tick.AreaEffectSystem;
import com.pgalaxyp.fragmento.system.temporal.ScheduledTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.UUID;
import java.util.function.Predicate;

public final class BasicFluteImpactTask extends ScheduledTask {

    private final SimulatedEntity entity;
    private final ServerLevel level;
    private final AreaEffectSystem areaSystem;
    private final AreaEffectComponent area;

    public BasicFluteImpactTask(
            long startTick,
            SimulatedEntity entity,
            ServerLevel level,
            AreaEffectSystem areaSystem
    ) {
        super(startTick, 2);
        this.entity = entity;
        this.level = level;

        this.area = new AreaEffectComponent(
                new AABB(
                        entity.x() - 0.6,
                        entity.y() - 0.6,
                        entity.z() - 0.6,
                        entity.x() + 0.6,
                        entity.y() + 0.6,
                        entity.z() + 0.6
                ),
                2,
                1
        );

        this.areaSystem = areaSystem;
    }

    @Override
    protected void execute(long now) {
        if (!entity.isAlive()) {
            finish();
            return;
        }

        Predicate<LivingEntity> filter = e -> {
            UUID owner = entity.ownerId();
            if (owner == null) return true;
            return !owner.equals(e.getUUID());
        };

        areaSystem.execute(
                level,
                area,
                filter,
                (target, lvl) -> {
                    target.hurt(
                            lvl.damageSources().magic(),
                            4.0F
                    );
                    entity.kill();
                    finish();
                }
        );
    }
}