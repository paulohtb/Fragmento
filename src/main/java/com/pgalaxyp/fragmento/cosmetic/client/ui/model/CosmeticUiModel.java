package com.pgalaxyp.fragmento.cosmetic.client.ui.model;

import com.pgalaxyp.fragmento.cosmetic.client.state.ClientCosmetics;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.tier.client.state.TierClientState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CosmeticUiModel {

    private CosmeticUiModel() {}

    public static List<CosmeticUiEntry> build(UUID playerId, CosmeticSlot slot) {
        if (playerId == null || slot == null) return List.of();

        int tier = TierClientState.level();
        List<CosmeticEntry> entries = ClientCosmetics.entriesBySlot(playerId, slot);

        ArrayList<CosmeticUiEntry> out = new ArrayList<>(entries.size());
        for (CosmeticEntry e : entries) {
            if (e == null || e.info() == null) continue;
            boolean unlocked = tier >= e.info().requiredTier();
            out.add(new CosmeticUiEntry(e, unlocked));
        }

        return List.copyOf(out);
    }
}