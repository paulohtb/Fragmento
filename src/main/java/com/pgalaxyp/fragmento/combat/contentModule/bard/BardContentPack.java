package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.contentModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.*;

public enum BardContentPack implements ContentPack {
    INSTANCE;

    private static final TargetingSpec SPEC = new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 16.0, TargetingFallback.IMAGINARY_POINT);
    private static final DamageSpec NOTE_DAMAGE = new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR);
    private static final DamageSpec NOTE_2_DAMAGE = new DamageSpec(2, DamageType.MAGIC, DamageElement.AIR);

    @Override public Collection<AbilityDefinition> abilities() {
        return List.of(
                new AbilityDefinition(BardIds.FLUTE_NOTE, 10, 20),
                new AbilityDefinition(BardIds.FLUTE_NOTE_2, 10, 20)
        );
    }

    @Override public List<AbilityRule> abilityRules() {
        return List.of(new AbilityRule("bard.flute.combo", BardIds.BARD, BardIds.FLUTE, 1, BardIds.FLUTE_NOTE, BardIds.FLUTE_NOTE_2));
    }

    @Override public Map<WeaponId, AbilityId> primaryBindings() {
        return Map.of(BardIds.FLUTE, BardIds.FLUTE_NOTE);
    }

    @Override public Map<AbilityId, AbilityTriggerSpec> abilityTriggers() {
        return Map.of(
                BardIds.FLUTE_NOTE, new AbilityTriggerSpec(NOTE_DAMAGE, SPEC),
                BardIds.FLUTE_NOTE_2, new AbilityTriggerSpec(NOTE_2_DAMAGE, SPEC)
        );
    }
}