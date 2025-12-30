package com.pgalaxyp.fragmento.combat.old.system.entity.behavior;

import com.pgalaxyp.fragmento.combat.old.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.combat.old.system.entity.movement.MovementPlan;
import com.pgalaxyp.fragmento.combat.old.system.temporal.BehaviorPhaseMachine;

public abstract class TimedSpiritBehavior<P extends Enum<P>> implements SpiritBehavior {

    private final BehaviorPhaseMachine<SpiritContext, P> machine;

    protected TimedSpiritBehavior() {
        this.machine = new BehaviorPhaseMachine<>(
                this::onEnterPhase,
                this::onTickPhase
        );
    }

    protected final void startPhase(SpiritContext ctx, P next, int duration) {
        machine.start(ctx, next, duration);
    }

    protected final P phase() {
        return machine.phase();
    }

    protected final int time() {
        return machine.time();
    }

    protected final int duration() {
        return machine.duration();
    }

    @Override
    public final void tick(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        if (machine.phase() == null) {
            startInitialPhase(ctx);
        }
        machine.step(ctx);
        tickInternal(ctx, movement, look);
    }

    protected abstract void startInitialPhase(SpiritContext ctx);

    protected abstract void tickInternal(
            SpiritContext ctx,
            MovementPlan movement,
            LookPlan look
    );

    protected abstract void onEnterPhase(SpiritContext ctx, P phase, int duration);

    protected abstract void onTickPhase(SpiritContext ctx, P phase, int time, int duration);
}