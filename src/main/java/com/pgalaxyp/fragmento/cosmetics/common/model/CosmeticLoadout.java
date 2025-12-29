package com.pgalaxyp.fragmento.cosmetics.common.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CosmeticLoadout {

    public static final CosmeticLoadout EMPTY = new CosmeticLoadout(Collections.emptyMap());

    private final EnumMap<CosmeticSlot, CosmeticId> equipped;
    private final Map<CosmeticSlot, CosmeticId> view;

    public CosmeticLoadout(Map<CosmeticSlot, CosmeticId> equipped) {
        Objects.requireNonNull(equipped, "equipped");
        EnumMap<CosmeticSlot, CosmeticId> copy = new EnumMap<>(CosmeticSlot.class);
        copy.putAll(equipped);
        this.equipped = copy;
        this.view = Collections.unmodifiableMap(copy);
    }

    public Map<CosmeticSlot, CosmeticId> view() {
        return view;
    }

    public CosmeticId get(CosmeticSlot slot) {
        return equipped.get(slot);
    }

    public boolean isEmpty() {
        return equipped.isEmpty();
    }

    public CosmeticLoadout with(CosmeticSlot slot, CosmeticId id) {
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(id, "id");
        EnumMap<CosmeticSlot, CosmeticId> next = new EnumMap<>(CosmeticSlot.class);
        next.putAll(equipped);
        next.put(slot, id);
        return new CosmeticLoadout(next);
    }

    public CosmeticLoadout without(CosmeticSlot slot) {
        Objects.requireNonNull(slot, "slot");
        if (!equipped.containsKey(slot)) {
            return this;
        }
        EnumMap<CosmeticSlot, CosmeticId> next = new EnumMap<>(CosmeticSlot.class);
        next.putAll(equipped);
        next.remove(slot);
        return new CosmeticLoadout(next);
    }
}