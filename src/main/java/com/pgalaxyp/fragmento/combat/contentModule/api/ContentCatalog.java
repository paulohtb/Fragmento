package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.*;

public record ContentCatalog(Map<AbilityId, AbilityDefinition> abilities, List<AbilityRule> abilityRules, Map<WeaponId, AbilityId> primaryBindings, Map<AbilityId, AbilityTriggerSpec> abilityTriggers, AbilityTuning abilityTuning) {
    public ContentCatalog {
        abilities = Map.copyOf(Objects.requireNonNull(abilities));
        abilityRules = List.copyOf(Objects.requireNonNull(abilityRules));
        primaryBindings = Map.copyOf(Objects.requireNonNull(primaryBindings));
        abilityTriggers = Map.copyOf(Objects.requireNonNull(abilityTriggers));
        Objects.requireNonNull(abilityTuning);
    }

    public static ContentCatalog of(List<? extends ContentPack> packs) {
        Objects.requireNonNull(packs);
        var abilities = new LinkedHashMap<AbilityId, AbilityDefinition>();
        var rules = new ArrayList<AbilityRule>();
        var primary = new LinkedHashMap<WeaponId, AbilityId>();
        var triggers = new LinkedHashMap<AbilityId, AbilityTriggerSpec>();
        AbilityTuning tuning = null;
        for (ContentPack p : packs) {
            Objects.requireNonNull(p);
            for (AbilityDefinition d : requireAll(p.abilities())) putUnique(abilities, d.id(), d, "abilityId");
            rules.addAll(requireAll(p.abilityRules()));
            for (var e : requireAllEntries(p.primaryBindings()).entrySet()) putUnique(primary, e.getKey(), e.getValue(), "weaponId");
            for (var e : requireAllEntries(p.abilityTriggers()).entrySet()) putUnique(triggers, e.getKey(), e.getValue(), "abilityId(trigger)");
            AbilityTuning t = Objects.requireNonNull(p.abilityTuning());
            tuning = tuning == null ? t : tuning.equals(t) ? tuning : throwConflict();
        }
        validateRuleConflicts(rules);
        validateReferences(abilities, primary, triggers, rules);
        return new ContentCatalog(abilities, rules, primary, triggers, tuning == null ? AbilityTuning.DEFAULT : tuning);
    }

    private static <T> Collection<T> requireAll(Collection<T> c) {
        Objects.requireNonNull(c);
        for (T v : c) Objects.requireNonNull(v);
        return c;
    }

    private static <K, V> Map<K, V> requireAllEntries(Map<K, V> m) {
        Objects.requireNonNull(m);
        for (var e : m.entrySet()) if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
        return m;
    }

    private static void validateReferences(Map<AbilityId, AbilityDefinition> abilities, Map<WeaponId, AbilityId> primary, Map<AbilityId, AbilityTriggerSpec> triggers, List<AbilityRule> rules) {
        for (var e : primary.entrySet()) if (!abilities.containsKey(e.getValue()))
            throw new IllegalStateException("Unknown abilityId in primaryBindings weaponId=" + e.getKey() + " abilityId=" + e.getValue());
        for (var e : triggers.entrySet()) if (!abilities.containsKey(e.getKey()))
            throw new IllegalStateException("Unknown abilityId in abilityTriggers abilityId=" + e.getKey());
        for (var r : rules) {
            if (!abilities.containsKey(r.resultAbility()))
                throw new IllegalStateException("Unknown resultAbility in abilityRules ruleId=" + r.ruleId() + " abilityId=" + r.resultAbility());
            AbilityId base = r.baseAbility();
            if (base != null && !abilities.containsKey(base))
                throw new IllegalStateException("Unknown baseAbility in abilityRules ruleId=" + r.ruleId() + " abilityId=" + base);
        }
    }

    private static <K, V> void putUnique(Map<K, V> map, K key, V value, String label) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if (map.putIfAbsent(key, value) != null) throw new IllegalStateException("Duplicate " + label + "=" + key);
    }

    private static void validateRuleConflicts(List<AbilityRule> rules) {
        for (int i = 0; i < rules.size(); i++) {
            AbilityRule a = rules.get(i);
            for (int j = i + 1; j < rules.size(); j++) {
                AbilityRule b = rules.get(j);
                if (overlaps(a, b) && !a.resultAbility().equals(b.resultAbility()))
                    throw new IllegalStateException("Conflicting abilityRules: " + a.ruleId() + " vs " + b.ruleId());
            }
        }
    }

    private static boolean overlaps(AbilityRule a, AbilityRule b) {
        return compat(a.weaponId(), b.weaponId()) && compat(a.stepIndex(), b.stepIndex()) && compat(a.baseAbility(), b.baseAbility()) && compat(a.classId(), b.classId());
    }

    private static <T> boolean compat(T a, T b) { return a == null || b == null || a.equals(b); }
    private static <T> T throwConflict() { throw new IllegalStateException("Conflicting abilityTuning"); }
}