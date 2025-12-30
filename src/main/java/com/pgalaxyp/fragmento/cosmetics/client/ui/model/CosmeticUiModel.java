package com.pgalaxyp.fragmento.cosmetics.client.ui.model;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticDefinitionsSnapshot;
import java.util.*;

public final class CosmeticUiModel {

    private CosmeticUiModel() {}

    public static List<CosmeticUiEntry> build(
            UUID playerId,
            CosmeticSlot slot,
            CosmeticDefinitionsSnapshot defsSnap,
            CosmeticEntitlementClientView entitlementView,
            CosmeticLoadout loadout
    ) {
        if (playerId == null || slot == null || defsSnap == null || loadout == null) {
            return List.of();
        }

        Map<CosmeticSlot, List<CosmeticDefinition>> bySlot = defsSnap.bySlotView();
        List<CosmeticDefinition> defs = bySlot.get(slot);
        if (defs == null || defs.isEmpty()) {
            return List.of();
        }

        CosmeticId equippedId = loadout.get(slot);

        ArrayList<CosmeticUiEntry> out = new ArrayList<>(defs.size());
        for (CosmeticDefinition def : defs) {
            if (def == null) {
                continue;
            }

            boolean allowed = entitlementView != null && entitlementView.allowed(def);
            boolean equipped = Objects.equals(def.id(), equippedId);

            out.add(new CosmeticUiEntry(def, equipped, allowed));
        }

        return out;
    }
}