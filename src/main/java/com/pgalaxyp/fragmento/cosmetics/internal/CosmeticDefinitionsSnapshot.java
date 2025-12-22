package com.pgalaxyp.fragmento.cosmetics.internal;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;

public final class CosmeticDefinitionsSnapshot {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CosmeticDefinitionsSnapshot EMPTY = new CosmeticDefinitionsSnapshot(Collections.emptyMap(), Collections.emptyMap(), 0);

    private final Map<CosmeticId, CosmeticDefinition> byId;
    private final Map<CosmeticSlot, List<CosmeticDefinition>> bySlot;
    private final int dataVersion;

    public CosmeticDefinitionsSnapshot(Map<CosmeticId, CosmeticDefinition> byId, Map<CosmeticSlot, List<CosmeticDefinition>> bySlot, int dataVersion) {
        Objects.requireNonNull(byId, "byId");
        Objects.requireNonNull(bySlot, "bySlot");
        this.byId = Collections.unmodifiableMap(byId);
        EnumMap<CosmeticSlot, List<CosmeticDefinition>> slotCopy = new EnumMap<>(CosmeticSlot.class);
        for (Map.Entry<CosmeticSlot, List<CosmeticDefinition>> e : bySlot.entrySet()) {
            CosmeticSlot slot = Objects.requireNonNull(e.getKey(), "slot");
            List<CosmeticDefinition> defs = Objects.requireNonNull(e.getValue(), "defs");
            ArrayList<CosmeticDefinition> list = new ArrayList<>(defs.size());
            for (int i = 0; i < defs.size(); i++) {
                list.add(Objects.requireNonNull(defs.get(i), "def"));
            }
            slotCopy.put(slot, Collections.unmodifiableList(list));
        }
        this.bySlot = Collections.unmodifiableMap(slotCopy);
        this.dataVersion = dataVersion;
        LOGGER.info("CosmeticDefinitionsSnapshot criado, ids {}, slots {}, version {}", Integer.valueOf(this.byId.size()), Integer.valueOf(this.bySlot.size()), Integer.valueOf(this.dataVersion));
    }

    public Map<CosmeticId, CosmeticDefinition> byIdView() {
        return byId;
    }

    public Map<CosmeticSlot, List<CosmeticDefinition>> bySlotView() {
        return bySlot;
    }

    public int dataVersion() {
        return dataVersion;
    }
}