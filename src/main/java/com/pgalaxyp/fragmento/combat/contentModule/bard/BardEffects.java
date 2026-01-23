package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.damageModule.api.DamageElement;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageType;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectDef;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;

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