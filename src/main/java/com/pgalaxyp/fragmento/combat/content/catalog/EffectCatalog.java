package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public interface EffectCatalog {
    Optional<EffectDef> effect(EffectId effectId);
}