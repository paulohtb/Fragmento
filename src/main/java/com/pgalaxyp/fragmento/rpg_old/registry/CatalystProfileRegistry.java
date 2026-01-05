package com.pgalaxyp.fragmento.rpg_old.registry;

import com.pgalaxyp.fragmento.rpg_old.content.profile.CatalystProfile;
import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystFamilyId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CatalystProfileRegistry {

    private final Map<CatalystFamilyId, CatalystProfile> byFamily = new HashMap<>();

    public void register(CatalystProfile profile) {
        if (profile == null) return;
        CatalystFamilyId family = profile.family();
        if (family == null) return;
        byFamily.put(family, Objects.requireNonNull(profile));
    }

    public CatalystProfile resolve(CatalystFamilyId family) {
        if (family == null) return null;
        return byFamily.get(family);
    }
}