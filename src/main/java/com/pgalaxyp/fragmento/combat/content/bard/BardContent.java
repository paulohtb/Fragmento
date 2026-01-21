package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityDef;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.content.defaults.DefaultIds;
import com.pgalaxyp.fragmento.combat.targeting.api.*;
import java.util.Map;

public final class BardContent {

    public static Map<com.pgalaxyp.fragmento.combat.ability.api.AbilityId, AbilityDef> abilities(TargetingWithWorld targeting) {
        return Map.of(
                DefaultIds.ABILITY_FLUTE,
                new AbilityDef(
                        DefaultIds.ABILITY_FLUTE,
                        15,
                        0,
                        DefaultIds.EFFECT_FLUTE_MAGIC,
                        new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 8.0, TargetingFallback.SELF)
                ),
                DefaultIds.ABILITY_FLUTE_FINISHER,
                new AbilityDef(
                        DefaultIds.ABILITY_FLUTE_FINISHER,
                        18,
                        0,
                        DefaultIds.EFFECT_FLUTE_FINISHER_MAGIC,
                        new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 10.0, TargetingFallback.SELF)
                )
        );
    }

    private BardContent() {}
}