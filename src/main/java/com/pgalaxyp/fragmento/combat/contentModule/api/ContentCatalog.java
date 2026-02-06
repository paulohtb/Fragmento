package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassKit;
import java.util.*;

public record ContentCatalog(Map<AbilityId, AbilityDefinition> abilities, Map<ClassId, ClassKit> classKits, Map<AbilityId, AbilityTriggerSpec> abilityTriggers) {
    public ContentCatalog {
        abilities = Map.copyOf(Objects.requireNonNull(abilities));
        classKits = Map.copyOf(Objects.requireNonNull(classKits));
        abilityTriggers = Map.copyOf(Objects.requireNonNull(abilityTriggers));
    }

    public static ContentCatalog of(List<? extends ContentPack> packs) {
        Objects.requireNonNull(packs);
        var abilities = new LinkedHashMap<AbilityId, AbilityDefinition>();
        var kits = new LinkedHashMap<ClassId, ClassKit>();
        var triggers = new LinkedHashMap<AbilityId, AbilityTriggerSpec>();
        var owner = new HashMap<AbilityId, ClassId>();
        for (var p : packs) {
            Objects.requireNonNull(p);
            for (var d : requireAll(p.abilities())) putUnique(abilities, d.id(), d, "abilityId");
            for (var k : requireAll(p.classKits())) {
                putUnique(kits, k.id(), k, "classId");
                for (var a : k.abilities()) {
                    ClassId prev = owner.putIfAbsent(a, k.id());
                    if (prev != null && !prev.equals(k.id()))
                        throw new IllegalStateException("AbilityId owned by multiple classes abilityId=" + a + " classA=" + prev + " classB=" + k.id());
                }
            }
            for (var e : requireAllEntries(p.abilityTriggers()).entrySet()) putUnique(triggers, e.getKey(), e.getValue(), "abilityId(trigger)");
        }
        validateReferences(abilities, kits, triggers);
        return new ContentCatalog(abilities, kits, triggers);
    }

    private static void validateReferences(Map<AbilityId, AbilityDefinition> abilities, Map<ClassId, ClassKit> kits, Map<AbilityId, AbilityTriggerSpec> triggers) {
        for (var e : triggers.entrySet()) if (!abilities.containsKey(e.getKey()))
            throw new IllegalStateException("Unknown abilityId in abilityTriggers abilityId=" + e.getKey());
        for (var k : kits.values()) for (var a : k.abilities()) if (!abilities.containsKey(a))
            throw new IllegalStateException("Unknown abilityId in classKits classId=" + k.id() + " abilityId=" + a);
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