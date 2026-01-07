package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;

public final class BasicActionTimingRule implements ActionTimingRule {

    @Override
    public long computeEndsAt(ActionTimeline timeline, long startedAt) {
        return startedAt + Math.max(timeline.totalMillis(), 0L);
    }
}