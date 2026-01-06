package com.pgalaxyp.fragmento.rpg.core.domain.action;

public record ActionTimeline(
        long windup,
        long active,
        long recovery
) {}