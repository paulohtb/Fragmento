package com.pgalaxyp.fragmento.rpg.content;

import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.core.content.*;
import com.pgalaxyp.fragmento.rpg.combo.registry.*;
import java.util.*;

public final class DefaultCombos {

    public static void register(InMemoryComboCatalog catalog) {
        Objects.requireNonNull(catalog);

        ComboId fluteBasic = new ComboId("combo.bard.flute.basic");

        ComboPattern pattern = new ComboPattern(List.of(
                new ComboStep(0, ComboInput.PRIMARY, "anim.none"),
                new ComboStep(1, ComboInput.PRIMARY, "anim.none"),
                new ComboStep(2, ComboInput.PRIMARY, "anim.none")
        ));

        catalog.register(DefaultContent.FLUTE, fluteBasic, pattern);
    }

    private DefaultCombos() {}
}