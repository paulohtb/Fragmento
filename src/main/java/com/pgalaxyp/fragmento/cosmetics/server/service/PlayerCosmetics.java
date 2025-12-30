package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

final class PlayerCosmetics {

    private final EnumMap<CosmeticSlot, CosmeticId> equipped = new EnumMap<>(CosmeticSlot.class);
    private final ConcurrentHashMap<CosmeticId, Long> perCosmeticVersion = new ConcurrentHashMap<>();
    private final AtomicLong rosterVersion = new AtomicLong();

    PlayerCosmetics() {
        for (CosmeticSlot s : CosmeticSlot.values()) {
            equipped.put(s, null);
        }
    }

    synchronized EnumMap<CosmeticSlot, CosmeticId> equippedCopy() {
        EnumMap<CosmeticSlot, CosmeticId> copy = new EnumMap<>(CosmeticSlot.class);
        copy.putAll(equipped);
        return copy;
    }

    synchronized CosmeticId equipped(CosmeticSlot slot) {
        return slot == null ? null : equipped.get(slot);
    }

    synchronized void setEquipped(CosmeticSlot slot, CosmeticId id) {
        equipped.put(Objects.requireNonNull(slot, "slot"), id);
    }

    long rosterVersion() {
        return rosterVersion.get();
    }

    long bumpRoster() {
        return rosterVersion.incrementAndGet();
    }

    long bumpCosmetic(CosmeticId id) {
        if (id == null) return 0L;
        return perCosmeticVersion.merge(id, 1L, Long::sum);
    }

    long cosmeticVersion(CosmeticId id) {
        if (id == null) return 0L;
        Long v = perCosmeticVersion.get(id);
        return v == null ? 0L : v;
    }

    synchronized Map<CosmeticSlot, CosmeticId> equippedView() {
        return Map.copyOf(equipped);
    }
}