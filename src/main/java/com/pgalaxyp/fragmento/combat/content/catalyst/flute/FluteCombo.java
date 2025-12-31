package com.pgalaxyp.fragmento.combat.content.catalyst.flute;

import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboStep;

import java.util.List;

public final class FluteCombo {

    private FluteCombo() {}

    public static ComboDefinition create() {
        return new ComboDefinition(
                List.of(
                        new ComboStep(0),
                        new ComboStep(1),
                        new ComboStep(2)
                )
        );
    }
}