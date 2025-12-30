package com.pgalaxyp.fragmento.cosmetics.common.model;

import java.util.Objects;

public record CosmeticEntry(
        CosmeticInfo info,
        boolean equipped,
        long version
) {
    public CosmeticEntry {
        Objects.requireNonNull(info, "info");
        if (version < 0L) {
            throw new IllegalArgumentException("version");
        }
    }

    public CosmeticId id() {
        return info.id();
    }

    public CosmeticSlot slot() {
        return info.slot();
    }
}