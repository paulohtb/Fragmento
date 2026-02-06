package com.pgalaxyp.fragmento.combat.classModule.api;

import java.util.Objects;

public record ClassId(String value) { public ClassId { Objects.requireNonNull(value); } }