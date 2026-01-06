package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;

public interface ActionTimingRule {
    long computeEndsAt(ActionTimeline timeline, long startedAt);
}