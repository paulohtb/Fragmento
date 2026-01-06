package com.pgalaxyp.fragmento.rpg.core.content.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStep;
import com.pgalaxyp.fragmento.rpg.core.domain.zone.SpawnRule;
import java.util.List;

public final class FluteComboSequence {

    public static ComboSequence create() {
        return new ComboSequence(List.of(
                new ComboStep("flute_1", SpawnRule.AUTO),
                new ComboStep("flute_2", SpawnRule.OPPOSITE),
                new ComboStep("flute_3", SpawnRule.TOP)
        ));
    }
}