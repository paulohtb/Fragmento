package com.pgalaxyp.fragmento.cosmetics.shared;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTypeId;
import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import java.util.ArrayList;
import java.util.List;

public final class CosmeticsBuiltinDefinitions {

    private CosmeticsBuiltinDefinitions() {}

    public static List<CosmeticDefinition> loadAll() {
        ArrayList<CosmeticDefinition> out = new ArrayList<>(1);
        out.add(new CosmeticDefinition(
                CosmeticId.of("fragmento:red_cube_head"),
                CosmeticTypeId.of("builtin_cube"),
                CosmeticSlot.HEAD,
                TierLevel.TIER_0,
                0,
                true
        ));
        return out;
    }
}