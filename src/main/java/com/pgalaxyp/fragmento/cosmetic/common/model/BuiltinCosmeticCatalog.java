package com.pgalaxyp.fragmento.cosmetic.common.model;

import java.util.*;

public final class BuiltinCosmeticCatalog implements CosmeticCatalog {

    private final Map<CosmeticId, CosmeticInfo> byId;
    private final Map<CosmeticSlot, List<CosmeticInfo>> bySlot;
    private final int dataVersion;

    public BuiltinCosmeticCatalog() {
        this.dataVersion = 1;

        ArrayList<CosmeticInfo> all = new ArrayList<>();
        all.add(new CosmeticInfo(
                CosmeticId.of("fragmento:red_cube_head"),
                CosmeticSlot.HEAD,
                3,
                0,
                true,
                "fragmento:red_cube_head",
                "Red Cube Head"
        ));

        this.byId = all.stream().collect(java.util.stream.Collectors.toUnmodifiableMap(CosmeticInfo::id, x -> x));

        EnumMap<CosmeticSlot, ArrayList<CosmeticInfo>> tmp = new EnumMap<>(CosmeticSlot.class);
        for (CosmeticSlot s : CosmeticSlot.values()) {
            tmp.put(s, new ArrayList<>());
        }
        for (CosmeticInfo i : all) {
            tmp.get(i.slot()).add(i);
        }

        EnumMap<CosmeticSlot, List<CosmeticInfo>> frozen = new EnumMap<>(CosmeticSlot.class);
        for (CosmeticSlot s : CosmeticSlot.values()) {
            ArrayList<CosmeticInfo> list = tmp.get(s);
            list.sort(Comparator.comparingInt(CosmeticInfo::sort).thenComparing(a -> a.id().value()));
            frozen.put(s, List.copyOf(list));
        }

        this.bySlot = java.util.Collections.unmodifiableMap(frozen);
    }

    @Override
    public CosmeticInfo get(CosmeticId id) {
        if (id == null) return null;
        return byId.get(id);
    }

    @Override
    public List<CosmeticInfo> bySlot(CosmeticSlot slot) {
        Objects.requireNonNull(slot, "slot");
        List<CosmeticInfo> list = bySlot.get(slot);
        return list == null ? List.of() : list;
    }

    @Override
    public Collection<CosmeticInfo> all() {
        return byId.values();
    }

    @Override
    public int dataVersion() {
        return dataVersion;
    }
}