package com.pgalaxyp.fragmento.rpg.core.domain.action;

public record ActionTimeline(
        long windupMillis,
        long activeMillis,
        long recoveryMillis
) {}