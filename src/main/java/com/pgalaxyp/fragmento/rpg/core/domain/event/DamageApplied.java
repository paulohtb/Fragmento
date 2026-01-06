package com.pgalaxyp.fragmento.rpg.core.domain.event;

public record DamageApplied(
        long sourceActorId,
        long targetActorId,
        double amount
) {}