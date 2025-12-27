package com.pgalaxyp.fragmento.cosmetics.client.ui;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.client.CosmeticsClientState;
import com.pgalaxyp.fragmento.cosmetics.client.render.CosmeticsClientRegistryAccess;
import com.pgalaxyp.fragmento.cosmetics.internal.snapshot.CosmeticDefinitionsSnapshot;
import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.client.TierClientState;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;

public final class CosmeticUiModel {

    private CosmeticUiModel() {}

    public static List<CosmeticUiEntry> build(CosmeticSlot slot) {
        if (slot == null) return List.of();

        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) return List.of();

        UUID selfId = mc.player.getUUID();

        CosmeticDefinitionsSnapshot snap = CosmeticsClientRegistryAccess.registry().snapshot();
        Tier tier = TierClientState.get();

        CosmeticLoadout loadout = CosmeticsClientState.getEffective(selfId);

        List<CosmeticDefinition> defs = snap.bySlotView().getOrDefault(slot, List.of());
        ArrayList<CosmeticUiEntry> out = new ArrayList<>(defs.size());

        for (CosmeticDefinition def : defs) {
            if (def == null) continue;

            boolean allowed;
            if (def.requiredTier().value() == 0) {
                allowed = true;
            } else {
                allowed = tier.active() && tier.allows(def.requiredTier());
            }

            CosmeticId equippedId = loadout.get(slot);
            boolean equipped = def.id().equals(equippedId);

            out.add(new CosmeticUiEntry(def, equipped, allowed));
        }

        return out;
    }
}