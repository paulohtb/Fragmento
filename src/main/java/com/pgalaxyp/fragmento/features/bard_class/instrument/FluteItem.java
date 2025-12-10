package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.features.bard_class.ability.NormalAbility;
import com.pgalaxyp.fragmento.features.bard_class.ability.SpecialAbility;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityBase;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.FluteSpiritRegistry;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.FluteSpirit;
import java.util.List;

public class FluteItem extends InstrumentBase {

    private static final double SPECIAL_RANGE = 12.0D;

    public FluteItem(Properties props) {
        super(props, createAbilities());
    }

    private static List<AbilityBase> createAbilities() {
        return List.of(
                new NormalAbility<>(
                        level -> new FluteSpirit(FluteSpiritRegistry.FLUTE_SPIRIT.get(), level),
                        level -> new FluteSpirit(FluteSpiritRegistry.FLUTE_SPIRIT.get(), level),
                        35,
                        50
                ),
                new SpecialAbility<>(
                        level -> new FluteSpirit(FluteSpiritRegistry.FLUTE_SPIRIT.get(), level),
                        100
                )
        );
    }
}
