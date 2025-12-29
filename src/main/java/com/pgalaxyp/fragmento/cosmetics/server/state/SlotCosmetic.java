package com.pgalaxyp.fragmento.cosmetics.server.state;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;

public record SlotCosmetic(
        CosmeticId base,
        CosmeticId forced
) {
    public CosmeticId effective() {
        return forced != null ? forced : base;
    }

    public SlotCosmetic withBase(CosmeticId id) {
        return new SlotCosmetic(id, forced);
    }

    public SlotCosmetic withoutBase() {
        return new SlotCosmetic(null, forced);
    }

    public SlotCosmetic withForced(CosmeticId id) {
        return new SlotCosmetic(base, id);
    }

    public SlotCosmetic withoutForced() {
        return new SlotCosmetic(base, null);
    }

    public boolean isEmpty() {
        return base == null && forced == null;
    }
}