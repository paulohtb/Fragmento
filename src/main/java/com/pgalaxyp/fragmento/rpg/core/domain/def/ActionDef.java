package com.pgalaxyp.fragmento.rpg.core.domain.def;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.CycleSpec;
import java.util.List;

public record ActionDef(
        ActionId id,
        ActionKind kind,
        CycleSpec cycle,
        List<EffectId> effectSequence
) {
    public ActionDef {
        if (id == null || kind == null || cycle == null || effectSequence == null) {
            throw new IllegalArgumentException();
        }
        if (effectSequence.isEmpty()) {
            throw new IllegalArgumentException();
        }
        for (EffectId effectId : effectSequence) {
            if (effectId == null) {
                throw new IllegalArgumentException();
            }
        }

        if (kind == ActionKind.COMBO) {
            int size = effectSequence.size();
            int min = cycle.combo().minStepsTotal();
            int max = cycle.combo().maxStepsTotal();
            if (size < min || size > max) {
                throw new IllegalArgumentException();
            }
        }

        effectSequence = List.copyOf(effectSequence);
    }
}