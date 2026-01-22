package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.effect.api.EffectDef;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.effect.api.EffectId;

import java.util.Map;

public final class BardEffects {
    public static Map<EffectId, EffectDef> effects() {
        return Map.of(
                BardIds.FLUTE_NOTE_DAMAGE,
                EffectDef.of(
                        BardIds.FLUTE_NOTE_DAMAGE,
                        new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR)
                ),
                BardIds.FLUTE_NOTE_2_DAMAGE,
                EffectDef.of(
                        BardIds.FLUTE_NOTE_2_DAMAGE,
                        new DamageSpec(2, DamageType.MAGIC, DamageElement.AIR)
                )
        );
    }

    private BardEffects() {}
}