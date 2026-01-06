package com.pgalaxyp.fragmento.rpg.gameplay.state;

import com.pgalaxyp.fragmento.rpg.gameplay.weapon.WeaponDef;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class WeaponRepository {

    private final Map<Long, String> equipped = new HashMap<>();
    private final Map<String, WeaponDef> defs = new HashMap<>();
    private final Map<Long, Long> lastUseNanos = new HashMap<>();

    public void registerDef(WeaponDef def) {
        defs.put(def.weaponId(), def);
    }

    public void equip(long actorId, String weaponId) {
        equipped.put(actorId, weaponId);
    }

    public Optional<String> equippedWeapon(long actorId) {
        return Optional.ofNullable(equipped.get(actorId));
    }

    public Optional<WeaponDef> equippedDef(long actorId) {
        var id = equipped.get(actorId);
        if (id == null) return Optional.empty();
        return Optional.ofNullable(defs.get(id));
    }

    public boolean canUse(long actorId, long nowNanos) {
        var def = equippedDef(actorId).orElse(null);
        if (def == null) return false;

        var last = lastUseNanos.getOrDefault(actorId, 0L);
        var cdNanos = (long) (def.baseCooldownSeconds() * 1_000_000_000L);
        return nowNanos - last >= cdNanos;
    }

    public void markUsed(long actorId, long nowNanos) {
        lastUseNanos.put(actorId, nowNanos);
    }
}