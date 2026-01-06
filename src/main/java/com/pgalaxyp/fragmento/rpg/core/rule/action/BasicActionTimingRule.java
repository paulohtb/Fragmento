package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;

public final class BasicActionTimingRule implements ActionTimingRule {

    @Override
    public long computeEndsAt(ActionTimeline timeline, long startedAt) {
        var duration = timeline.windup() + timeline.active() + timeline.recovery();
        return startedAt + Math.max((long) duration, 0L);
    }
}