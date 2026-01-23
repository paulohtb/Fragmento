package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectDef;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentPack;
import java.util.*;

public enum BardContentPack implements ContentPack {
    INSTANCE;

    private static final TargetingSpec SPEC = new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 16.0, TargetingFallback.IMAGINARY_POINT);

    @Override
    public Collection<AbilityDefinition> abilities() {
        return List.of(
                new AbilityDefinition(BardIds.FLUTE_NOTE, 10, 20, BardIds.FLUTE_NOTE_DAMAGE, SPEC),
                new AbilityDefinition(BardIds.FLUTE_NOTE_2, 10, 20, BardIds.FLUTE_NOTE_2_DAMAGE, SPEC)
        );
    }

    @Override
    public Collection<EffectDef> effects() {
        return List.of(
                EffectDef.of(BardIds.FLUTE_NOTE_DAMAGE, BardEffects.effects().get(BardIds.FLUTE_NOTE_DAMAGE).damage()),
                EffectDef.of(BardIds.FLUTE_NOTE_2_DAMAGE, BardEffects.effects().get(BardIds.FLUTE_NOTE_2_DAMAGE).damage())
        );
    }

    @Override
    public List<AbilityRule> abilityRules() {
        return List.of(new AbilityRule("bard.flute.combo", BardIds.BARD, BardIds.FLUTE, 1, BardIds.FLUTE_NOTE, BardIds.FLUTE_NOTE_2));
    }

    @Override
    public Map<com.pgalaxyp.fragmento.combat.weaponModule.WeaponId, com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId> primaryBindings() {
        return Map.of(BardIds.FLUTE, BardIds.FLUTE_NOTE);
    }
}