package com.pgalaxyp.fragmento.cosmetics.client.ui.model;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticEntry;

import java.util.Objects;

public record CosmeticUiEntry(
        CosmeticEntry entry,
        boolean unlocked
) {
    public CosmeticUiEntry {
        Objects.requireNonNull(entry, "entry");
    }
}