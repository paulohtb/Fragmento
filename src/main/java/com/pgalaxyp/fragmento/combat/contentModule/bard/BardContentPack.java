package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.contentModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.actionModule.api.ActionSlot;
import java.util.*;

public enum BardContentPack implements ContentPack {
    INSTANCE;

    private static final TargetingSpec SPEC = new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 16.0, TargetingFallback.IMAGINARY_POINT);
    private static final DamageSpec NOTE_DAMAGE = new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR);
    private static final DamageSpec NOTE_2_DAMAGE = new DamageSpec(2, DamageType.MAGIC, DamageElement.AIR);

    private static final ClassKit KIT = new ClassKit(BardIds.BARD, Set.of(BardIds.FLUTE), Set.of(BardIds.FLUTE_NOTE, BardIds.FLUTE_NOTE_2), Map.of(ActionSlot.PRIMARY, BardIds.FLUTE_NOTE));

    @Override public Collection<AbilityDefinition> abilities() {
        return List.of(
                new AbilityDefinition(BardIds.FLUTE_NOTE, 10, 20),
                new AbilityDefinition(BardIds.FLUTE_NOTE_2, 10, 20)
        );
    }

    @Override public Collection<ClassKit> classKits() { return List.of(KIT); }

    @Override public Map<AbilityId, AbilityTriggerSpec> abilityTriggers() {
        return Map.of(
                BardIds.FLUTE_NOTE, new AbilityTriggerSpec(NOTE_DAMAGE, SPEC),
                BardIds.FLUTE_NOTE_2, new AbilityTriggerSpec(NOTE_2_DAMAGE, SPEC)
        );
    }
}