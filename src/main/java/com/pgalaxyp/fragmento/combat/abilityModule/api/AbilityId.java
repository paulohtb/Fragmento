package com.pgalaxyp.fragmento.combat.abilityModule.api;

import java.util.Objects;

public record AbilityId(String value) { public AbilityId { Objects.requireNonNull(value); } }