package com.pgalaxyp.fragmento.rpg.core.domain.damage;

public record DamageRequest(long sourceActorId, long targetActorId, double amount, DamageType type) {}