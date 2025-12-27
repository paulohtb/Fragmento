package com.pgalaxyp.fragmento.cosmetics.internal.snapshot;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CosmeticDefinitionsSnapshot {

    public static final CosmeticDefinitionsSnapshot EMPTY =
            new CosmeticDefinitionsSnapshot(
                    Collections.emptyMap(),
                    Collections.emptyMap(),
                    0
            );

    private final Map<CosmeticId, CosmeticDefinition> byId;
    private final Map<CosmeticSlot, List<CosmeticDefinition>> bySlot;
    private final int dataVersion;

    public CosmeticDefinitionsSnapshot(
            Map<CosmeticId, CosmeticDefinition> byId,
            Map<CosmeticSlot, List<CosmeticDefinition>> bySlot,
            int dataVersion
    ) {
        Objects.requireNonNull(byId, "byId");
        Objects.requireNonNull(bySlot, "bySlot");

        this.byId = Map.copyOf(byId);

        EnumMap<CosmeticSlot, List<CosmeticDefinition>> slotCopy = new EnumMap<>(CosmeticSlot.class);
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            List<CosmeticDefinition> list = bySlot.get(slot);
            if (list == null || list.isEmpty()) {
                slotCopy.put(slot, Collections.emptyList());
            } else {
                slotCopy.put(slot, List.copyOf(list));
            }
        }

        this.bySlot = Collections.unmodifiableMap(slotCopy);
        this.dataVersion = dataVersion;
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