package com.pgalaxyp.fragmento.cosmetics.common.registry;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CosmeticSnapshotBuilder {

    private static final Comparator<CosmeticDefinition> ORDER = new DefinitionOrder();

    public CosmeticDefinitionsSnapshot build(List<CosmeticDefinition> definitions, int dataVersion) {
        Map<CosmeticId, CosmeticDefinition> byId = new HashMap<>();
        EnumMap<CosmeticSlot, ArrayList<CosmeticDefinition>> bySlot = new EnumMap<>(CosmeticSlot.class);

        for (CosmeticSlot slot : CosmeticSlot.values()) {
            bySlot.put(slot, new ArrayList<>());
        }

        if (definitions != null) {
            for (CosmeticDefinition def : definitions) {
                if (def == null) {
                    continue;
                }
                CosmeticId id = def.id();
                CosmeticSlot slot = def.slot();
                if (id == null || slot == null) {
                    continue;
                }
                if (byId.containsKey(id)) {
                    continue;
                }
                byId.put(id, def);
                bySlot.get(slot).add(def);
            }
        }

        EnumMap<CosmeticSlot, List<CosmeticDefinition>> frozenBySlot = new EnumMap<>(CosmeticSlot.class);
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            ArrayList<CosmeticDefinition> list = bySlot.get(slot);
            if (list == null || list.isEmpty()) {
                frozenBySlot.put(slot, List.of());
            } else {
                list.sort(ORDER);
                frozenBySlot.put(slot, List.copyOf(list));
            }
        }

        return new CosmeticDefinitionsSnapshot(byId, frozenBySlot, dataVersion);
    }

    private static final class DefinitionOrder implements Comparator<CosmeticDefinition> {

        @Override
        public int compare(CosmeticDefinition a, CosmeticDefinition b) {
            if (a == b) {
                return 0;
            }
            if (a == null) {
                return 1;
            }
            if (b == null) {
                return -1;
            }

            int pa = a.priority();
            int pb = b.priority();
            if (pa != pb) {
                return Integer.compare(pb, pa);
            }

            int ra = a.requiredLevel();
            int rb = b.requiredLevel();
            if (ra != rb) {
                return Integer.compare(ra, rb);
            }

            String ia = a.id() == null ? "" : a.id().value();
            String ib = b.id() == null ? "" : b.id().value();
            return ia.compareTo(ib);
        }
    }
}