package com.pgalaxyp.fragmento.cosmetic.client.ui.model;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticEntry;

import java.util.Objects;

public record CosmeticUiEntry(
        CosmeticEntry entry,
        boolean unlocked
) {
    public CosmeticUiEntry {
        Objects.requireNonNull(entry, "entry");
    }
}