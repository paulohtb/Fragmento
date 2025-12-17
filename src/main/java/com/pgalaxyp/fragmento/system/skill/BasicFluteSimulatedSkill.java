package com.pgalaxyp.fragmento.system.skill;

import com.pgalaxyp.fragmento.system.entity.host.SimulatedEntity;
import com.pgalaxyp.fragmento.system.temporal.TemporalScheduler;
import com.pgalaxyp.fragmento.system.temporal.ScheduledTask;

import java.util.UUID;

public final class BasicFluteSimulatedSkill {

    private final TemporalScheduler scheduler;

    public BasicFluteSimulatedSkill(TemporalScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start(
            int entityId,
            UUID ownerId,
            double x,
            double y,
            double z
    ) {
        SimulatedEntity entity =
                new SimulatedEntity(entityId, ownerId, x, y, z);

        scheduler.schedule(
                new MovementTask(entity, 20)
        );
    }

    private static final class MovementTask extends ScheduledTask {

        private final SimulatedEntity entity;
        private int ticks;

        private MovementTask(SimulatedEntity entity, int duration) {
            super(0, 1);
            this.entity = entity;
            this.ticks = duration;
        }

        @Override
        protected void execute(long now) {
            if (!entity.isAlive()) {
                finish();
                return;
            }

            entity.move(0.0, 0.0, 0.25);
            ticks--;

            if (ticks <= 0) {
                entity.kill();
                finish();
            }
        }
    }
}