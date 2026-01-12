package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.input.api.ModInputContext;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public final class WeaponInputPolicy {

    public record WeaponInputRule(
            boolean dominateInput,
            boolean allowBlockBreak,
            boolean allowItemUse
    ) {
        public WeaponInputRule {}

        public static WeaponInputRule dominateAll() {
            return new WeaponInputRule(true, false, false);
        }

        public static WeaponInputRule allowAll() {
            return new WeaponInputRule(false, true, true);
        }
    }

    private final NavigableMap<WeaponId, WeaponInputRule> rules = new TreeMap<>();
    private final WeaponInputRule defaultRule;

    public WeaponInputPolicy(WeaponInputRule defaultRule) {
        if (defaultRule == null) {
            throw new IllegalArgumentException();
        }
        this.defaultRule = defaultRule;
    }

    public void register(WeaponId weaponId, WeaponInputRule rule) {
        if (weaponId == null || rule == null) {
            throw new IllegalArgumentException();
        }
        rules.put(weaponId, rule);
    }

    public WeaponInputRule resolve(ModInputContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }

        Optional<WeaponId> wid = context.activeWeaponId();
        if (wid.isEmpty()) {
            return defaultRule;
        }

        WeaponInputRule rule = rules.get(wid.get());
        if (rule == null) {
            return defaultRule;
        }
        return rule;
    }
}