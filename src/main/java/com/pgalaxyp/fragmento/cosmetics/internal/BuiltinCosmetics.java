package com.pgalaxyp.fragmento.cosmetics.internal;

import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTransform;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTypeId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public final class BuiltinCosmetics {
    private static final Map<ResourceLocation, CosmeticDefinition> DEFINITIONS_BY_RL;

    static {
        Map<ResourceLocation, CosmeticDefinition> map = new HashMap<>();

        ResourceLocation demoHaloId = ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "demo_halo");
        CosmeticDefinition demoHalo = new CosmeticDefinition(
                CosmeticId.of(demoHaloId),
                CosmeticTypeId.of(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "demo")),
                CosmeticSlot.HEAD,
                CosmeticTier.TIER_0,
                0,
                true,
                CosmeticTransform.IDENTITY
        );
        map.put(demoHaloId, demoHalo);

        DEFINITIONS_BY_RL = Collections.unmodifiableMap(map);
    }

    private BuiltinCosmetics() {
    }

    public static boolean isAllowed(ResourceLocation id) {
        if (id == null) return false;
        return DEFINITIONS_BY_RL.containsKey(id);
    }

    public static List<CosmeticDefinition> all() {
        if (DEFINITIONS_BY_RL.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(DEFINITIONS_BY_RL.values());
    }

    public static CosmeticDefinition defaultDefinition(ResourceLocation id) {
        if (id == null) return null;
        return DEFINITIONS_BY_RL.get(id);
    }

    public static Map<CosmeticSlot, List<CosmeticDefinition>> groupBySlot(List<CosmeticDefinition> defs) {
        EnumMap<CosmeticSlot, List<CosmeticDefinition>> bySlot = new EnumMap<>(CosmeticSlot.class);
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            bySlot.put(slot, new ArrayList<>());
        }
        if (defs == null) return bySlot;

        for (int i = 0; i < defs.size(); i++) {
            CosmeticDefinition d = defs.get(i);
            if (d == null) continue;
            List<CosmeticDefinition> list = bySlot.get(Objects.requireNonNull(d.slot(), "slot"));
            if (list != null) list.add(d);
        }
        return bySlot;
    }
}