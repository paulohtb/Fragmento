package com.pgalaxyp.fragmento.content.bard.entity;

public abstract class TimedSkillEntity<P extends Enum<P>> extends SkillEntity {

    private final PhaseMachine<P> machine;

    protected TimedSkillEntity(BardSkillEntityBase spirit) {
        super(spirit);
        this.machine = new PhaseMachine<>(
                (phase, duration) -> onEnterPhase(phase),
                (phase, time, duration) -> {
                    if (!spirit().level().isClientSide()) {
                        onTickPhase(phase);
                    }
                }
        );
    }

    protected final void startPhase(P next, int duration) {
        machine.start(next, duration);
    }

    protected final int time() {
        return machine.time();
    }

    protected final int duration() {
        return machine.duration();
    }

    protected final P phase() {
        return machine.phase();
    }

    @Override
    protected final void tickSkill() {
        machine.step();
    }

    protected abstract void onEnterPhase(P phase);

    protected abstract void onTickPhase(P phase);
}
