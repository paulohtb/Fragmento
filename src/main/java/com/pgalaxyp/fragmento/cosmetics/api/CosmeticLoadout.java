package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;

public final class CosmeticLoadout {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CosmeticLoadout EMPTY = new CosmeticLoadout(Collections.emptyMap());

    private final Map<CosmeticSlot, CosmeticId> equipped;

    public CosmeticLoadout(Map<CosmeticSlot, CosmeticId> equipped) {
        Objects.requireNonNull(equipped, "equipped");
        EnumMap<CosmeticSlot, CosmeticId> copy = new EnumMap<>(CosmeticSlot.class);
        for (Map.Entry<CosmeticSlot, CosmeticId> e : equipped.entrySet()) {
            CosmeticSlot slot = Objects.requireNonNull(e.getKey(), "slot");
            CosmeticId id = Objects.requireNonNull(e.getValue(), "cosmeticId");
            copy.put(slot, id);
        }
        this.equipped = Collections.unmodifiableMap(copy);
        LOGGER.debug("CosmeticLoadout criado, slots {}", Integer.valueOf(this.equipped.size()));
    }

    public Map<CosmeticSlot, CosmeticId> equippedView() {
        return equipped;
    }

    public CosmeticId get(CosmeticSlot slot) {
        if (slot == null) return null;
        return equipped.get(slot);
    }

    public boolean isEmpty() {
        return equipped.isEmpty();
    }

    public CosmeticLoadout with(CosmeticSlot slot, CosmeticId cosmeticId) {
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");
        EnumMap<CosmeticSlot, CosmeticId> next = new EnumMap<>(CosmeticSlot.class);
        next.putAll(this.equipped);
        next.put(slot, cosmeticId);
        CosmeticLoadout out = new CosmeticLoadout(next);
        LOGGER.info("CosmeticLoadout with, slot {}, cosmetic {}", slot.name(), cosmeticId);
        return out;
    }

    public CosmeticLoadout without(CosmeticSlot slot) {
        Objects.requireNonNull(slot, "slot");
        if (!this.equipped.containsKey(slot)) {
            LOGGER.debug("CosmeticLoadout without ignorado, slot {} sem item", slot.name());
            return this;
        }
        EnumMap<CosmeticSlot, CosmeticId> next = new EnumMap<>(CosmeticSlot.class);
        next.putAll(this.equipped);
        next.remove(slot);
        CosmeticLoadout out = new CosmeticLoadout(next);
        LOGGER.info("CosmeticLoadout without, slot {}", slot.name());
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CosmeticLoadout other)) return false;
        return this.equipped.equals(other.equipped);
    }

    @Override
    public int hashCode() {
        return equipped.hashCode();
    }

    @Override
    public String toString() {
        return "CosmeticLoadout" + equipped;
    }
}