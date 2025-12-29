package com.pgalaxyp.fragmento.cosmetics.client.ui.model;

import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientRegistries;
import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientState;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientViews;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.LevelAccessPolicies;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.LevelAccessPolicy;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticDefinitionsSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;

public final class CosmeticUiModel {

    private static final LevelAccessPolicy ACCESS = LevelAccessPolicies.DEFAULT;

    private CosmeticUiModel() {}

    public static List<CosmeticUiEntry> build(CosmeticSlot slot) {
        if (slot == null) {
            return List.of();
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) {
            return List.of();
        }

        UUID selfId = mc.player.getUUID();

        CosmeticDefinitionsSnapshot snap = CosmeticsClientRegistries.registry().snapshot();
        CosmeticEntitlementClientView view = CosmeticEntitlementClientViews.view();
        int level = view == null ? 0 : view.level();

        CosmeticLoadout loadout = CosmeticsClientState.getEffective(selfId);

        List<CosmeticDefinition> defs = snap.bySlotView().getOrDefault(slot, List.of());
        ArrayList<CosmeticUiEntry> out = new ArrayList<>(defs.size());

        for (CosmeticDefinition def : defs) {
            if (def == null) {
                continue;
            }

            boolean allowed = ACCESS.allowed(level, def.requiredLevel());

            CosmeticId equippedId = loadout.get(slot);
            boolean equipped = def.id().equals(equippedId);

            out.add(new CosmeticUiEntry(def, equipped, allowed));
        }

        return out;
    }
}