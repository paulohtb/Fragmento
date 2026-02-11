package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityDefinition;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackDefinition;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassKit;
import com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileId;
import com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileSpec;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ContentCatalog(
        Map<AbilityId, AbilityDefinition> abilities,
        Map<ClassId, ClassKit> classKits,
        Map<AbilityId, AbilityTriggerSpec> abilityTriggers,
        Map<WeaponId, BasicAttackDefinition> weaponBasicAttacks,
        Map<ProjectileId, ProjectileSpec> projectiles
) {
    public ContentCatalog {
        abilities = Map.copyOf(Objects.requireNonNull(abilities));
        classKits = Map.copyOf(Objects.requireNonNull(classKits));
        abilityTriggers = Map.copyOf(Objects.requireNonNull(abilityTriggers));
        weaponBasicAttacks = Map.copyOf(Objects.requireNonNull(weaponBasicAttacks));
        projectiles = Map.copyOf(Objects.requireNonNull(projectiles));
    }

    public static ContentCatalog of(List<? extends ContentPack> packs) {
        Objects.requireNonNull(packs);

        var abilities = new LinkedHashMap<AbilityId, AbilityDefinition>();
        var kits = new LinkedHashMap<ClassId, ClassKit>();
        var triggers = new LinkedHashMap<AbilityId, AbilityTriggerSpec>();
        var basicAttacks = new LinkedHashMap<WeaponId, BasicAttackDefinition>();
        var projectiles = new LinkedHashMap<ProjectileId, ProjectileSpec>();

        var abilityOwner = new HashMap<AbilityId, ClassId>();

        for (var p : packs) {
            Objects.requireNonNull(p);

            for (var d : requireAll(p.abilities())) putUnique(abilities, d.id(), d, "abilityId");
            for (var k : requireAll(p.classKits())) {
                putUnique(kits, k.id(), k, "classId");
                for (var a : k.abilities()) {
                    ClassId prev = abilityOwner.putIfAbsent(a, k.id());
                    if (prev != null && !prev.equals(k.id()))
                        throw new IllegalStateException("AbilityId owned by multiple classes abilityId=" + a + " classA=" + prev + " classB=" + k.id());
                }
            }
            for (var e : requireAllEntries(p.abilityTriggers()).entrySet()) putUnique(triggers, e.getKey(), e.getValue(), "abilityId(trigger)");

            for (var ps : requireAll(p.projectiles())) putUnique(projectiles, ps.id(), ps, "projectileId");
            for (var e : requireAllEntries(p.weaponBasicAttacks()).entrySet()) putUnique(basicAttacks, e.getKey(), e.getValue(), "weaponId(basicAttack)");
        }

        validateReferences(abilities, kits, triggers, basicAttacks, projectiles);
        return new ContentCatalog(abilities, kits, triggers, basicAttacks, projectiles);
    }

    private static void validateReferences(
            Map<AbilityId, AbilityDefinition> abilities,
            Map<ClassId, ClassKit> kits,
            Map<AbilityId, AbilityTriggerSpec> triggers,
            Map<WeaponId, BasicAttackDefinition> basicAttacks,
            Map<ProjectileId, ProjectileSpec> projectiles
    ) {
        for (var e : triggers.entrySet()) if (!abilities.containsKey(e.getKey()))
            throw new IllegalStateException("Unknown abilityId in abilityTriggers abilityId=" + e.getKey());

        for (var k : kits.values()) {
            for (var a : k.abilities()) if (!abilities.containsKey(a))
                throw new IllegalStateException("Unknown abilityId in classKits classId=" + k.id() + " abilityId=" + a);
            for (var w : k.weapons()) if (!basicAttacks.containsKey(w))
                throw new IllegalStateException("Weapon without basic attack in classKits classId=" + k.id() + " weaponId=" + w);
        }

        for (var e : basicAttacks.entrySet()) {
            var def = e.getValue();
            for (var step : def.steps()) if (!projectiles.containsKey(step.projectileId()))
                throw new IllegalStateException("Unknown projectileId in weaponBasicAttacks weaponId=" + e.getKey() + " projectileId=" + step.projectileId());
        }
    }

    private static <T> Collection<T> requireAll(Collection<T> c) {
        Objects.requireNonNull(c);
        for (var v : c) Objects.requireNonNull(v);
        return c;
    }

    private static <K, V> Map<K, V> requireAllEntries(Map<K, V> m) {
        Objects.requireNonNull(m);
        for (var e : m.entrySet()) if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
        return m;
    }

    private static <K, V> void putUnique(Map<K, V> map, K key, V value, String label) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if (map.putIfAbsent(key, value) != null) throw new IllegalStateException("Duplicate " + label + "=" + key);
    }
}
