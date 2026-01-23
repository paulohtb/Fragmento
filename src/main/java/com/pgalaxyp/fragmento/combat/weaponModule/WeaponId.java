package com.pgalaxyp.fragmento.combat.weaponModule;

import java.util.Objects;

public record WeaponId(String value) implements Comparable<WeaponId> {
    public WeaponId { Objects.requireNonNull(value); }
    @Override public int compareTo(WeaponId id) { return value.compareTo(Objects.requireNonNull(id).value); }
}