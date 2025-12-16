package com.pgalaxyp.fragmento.gameplay.entity;

import com.pgalaxyp.fragmento.content.bard.entity.BardSkillEntityBase;

public abstract class TimedSkillEntity<P extends Enum<P>> {

    private final BardSkillEntityBase spirit;

    private P phase;
    private int time;
    private int duration;

    protected TimedSkillEntity(BardSkillEntityBase spirit) {
        this.spirit = spirit;
    }

    protected final BardSkillEntityBase spirit() {
        return spirit;
    }

    protected final int time() {
        return time;
    }

    protected final int duration() {
        return duration;
    }

    protected final void startPhase(P next, int durationTicks) {
        phase = next;
        duration = Math.max(0, durationTicks);
        time = 0;
        onEnterPhase(next);
    }

    public final void tick() {
        if (phase == null) return;

        onTickPhase(phase);
        time++;
    }

    protected abstract void onEnterPhase(P phase);

    protected abstract void onTickPhase(P phase);
}
