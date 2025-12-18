package com.pgalaxyp.fragmento.system.entity.behavior;

import com.pgalaxyp.fragmento.system.temporal.BehaviorPhaseMachine;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;

public abstract class TimedSpiritBehavior<P extends Enum<P>> implements SpiritBehavior {

    private final BehaviorPhaseMachine<P> machine;

    protected TimedSpiritBehavior() {
        this.machine = new BehaviorPhaseMachine<>(
                (phase, duration) -> onEnterPhase(phase),
                this::onTickPhase
        );
    }

    protected final void startPhase(P next, int duration) {
        machine.start(next, duration);
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
        machine.step();
        tickInternal(ctx, movement, look);
    }

    protected abstract void startInitialPhase(SpiritContext ctx);

    protected abstract void tickInternal(
            SpiritContext ctx,
            MovementPlan movement,
            LookPlan look
    );

    protected abstract void onEnterPhase(P phase);

    protected abstract void onTickPhase(P phase, int time, int duration);
}