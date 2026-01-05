package com.pgalaxyp.fragmento.rpg.gameplay.damage;

public record DamageRequest(long sourceActorId, long targetActorId, double amount, DamageType type) {}