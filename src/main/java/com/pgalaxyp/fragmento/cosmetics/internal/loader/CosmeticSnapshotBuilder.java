package com.pgalaxyp.fragmento.cosmetics.internal.loader;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.internal.snapshot.CosmeticDefinitionsSnapshot;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CosmeticSnapshotBuilder {

    public CosmeticDefinitionsSnapshot build(
            List<CosmeticDefinition> definitions,
            int dataVersion
    ) {
        Map<CosmeticId, CosmeticDefinition> byId = new HashMap<>();
        EnumMap<CosmeticSlot, List<CosmeticDefinition>> bySlot =
                new EnumMap<>(CosmeticSlot.class);

        for (CosmeticSlot slot : CosmeticSlot.values()) {
            bySlot.put(slot, new ArrayList<>());
        }

        for (CosmeticDefinition def : definitions) {
            if (def == null) continue;
            if (byId.containsKey(def.id())) continue;

            byId.put(def.id(), def);
            bySlot.get(def.slot()).add(def);
        }

        return new CosmeticDefinitionsSnapshot(byId, bySlot, dataVersion);
    }
}