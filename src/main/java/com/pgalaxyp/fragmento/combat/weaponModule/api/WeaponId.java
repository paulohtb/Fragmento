package com.pgalaxyp.fragmento.combat.weaponModule.api;

import java.util.Objects;

public record WeaponId(String value) { public WeaponId { Objects.requireNonNull(value); } }