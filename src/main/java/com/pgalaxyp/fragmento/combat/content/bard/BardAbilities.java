package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import java.util.Map;

public final class BardAbilities {
    public static Map<AbilityId, AbilityDef> abilities() {
        TargetingSpec spec = new TargetingSpec(
                TargetingMode.RAYCAST_SINGLE,
                8.0,
                TargetingFallback.SELF
        );

        return Map.of(
                BardIds.FLUTE_NOTE,
                new AbilityDef(
                        BardIds.FLUTE_NOTE,
                        10,
                        20,
                        BardIds.FLUTE_NOTE_DAMAGE,
                        spec
                ),
                BardIds.FLUTE_NOTE_2,
                new AbilityDef(
                        BardIds.FLUTE_NOTE_2,
                        10,
                        20,
                        BardIds.FLUTE_NOTE_2_DAMAGE,
                        spec
                )
        );
    }

    private BardAbilities() {}
}