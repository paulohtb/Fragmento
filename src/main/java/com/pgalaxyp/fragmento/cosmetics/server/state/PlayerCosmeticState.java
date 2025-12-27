package com.pgalaxyp.fragmento.cosmetics.server.state;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.tiers.api.Tier;
import java.util.EnumMap;

public record PlayerCosmeticState(
        CosmeticLoadout base,
        CosmeticLoadout forced,
        long version
) {

    public static final PlayerCosmeticState EMPTY =
            new PlayerCosmeticState(CosmeticLoadout.EMPTY, CosmeticLoadout.EMPTY, 0L);

    public CosmeticLoadoutSnapshot snapshot() {
        return CosmeticLoadoutSnapshot.of(resolveEffective(), version);
    }

    public CosmeticLoadout resolveEffective() {
        EnumMap<CosmeticSlot, CosmeticId> out = new EnumMap<>(CosmeticSlot.class);
        out.putAll(base.view());
        out.putAll(forced.view());
        return new CosmeticLoadout(out);
    }

    public PlayerCosmeticState revalidate(CosmeticRegistry registry, Tier tier) {
        EnumMap<CosmeticSlot, CosmeticId> valid = new EnumMap<>(CosmeticSlot.class);

        for (var e : base.view().entrySet()) {
            CosmeticDefinition def = registry.get(e.getValue());
            if (def != null && tier.allows(def.requiredTier())) {
                valid.put(e.getKey(), e.getValue());
            }
        }

        return new PlayerCosmeticState(new CosmeticLoadout(valid), forced, version + 1L);
    }
}