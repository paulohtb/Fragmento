package com.pgalaxyp.fragmento.rpg.engine.catalyst;

import com.pgalaxyp.fragmento.rpg.domain.id.CatalystFamilyId;

import java.util.HashMap;
import java.util.Map;

public final class CatalystEffectRegistry {

    private static final Map<CatalystFamilyId, CatalystEffectAdapter> BY_FAMILY = new HashMap<>();

    public static void register(CatalystFamilyId family, CatalystEffectAdapter adapter) {
        if (family != null && adapter != null) {
            BY_FAMILY.put(family, adapter);
        }
    }

    public static CatalystEffectAdapter resolve(CatalystFamilyId family) {
        return BY_FAMILY.get(family);
    }

    private CatalystEffectRegistry() {}
}