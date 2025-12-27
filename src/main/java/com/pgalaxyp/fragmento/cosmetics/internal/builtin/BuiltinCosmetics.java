package com.pgalaxyp.fragmento.cosmetics.internal.builtin;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTypeId;
import com.pgalaxyp.fragmento.cosmetics.internal.loader.CosmeticSnapshotBuilder;
import com.pgalaxyp.fragmento.cosmetics.internal.snapshot.CosmeticDefinitionsSnapshot;
import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import java.util.List;

public final class BuiltinCosmetics {

    private static final CosmeticDefinitionsSnapshot SNAPSHOT = build();

    private BuiltinCosmetics() {}

    public static CosmeticDefinitionsSnapshot snapshot() {
        return SNAPSHOT;
    }

    private static CosmeticDefinitionsSnapshot build() {
        CosmeticDefinition redHead =
                new CosmeticDefinition(
                        CosmeticId.of("fragmento:red_cube_head"),
                        CosmeticTypeId.of("builtin"),
                        CosmeticSlot.HEAD,
                        TierLevel.TIER_0,
                        0,
                        true
                );

        CosmeticSnapshotBuilder b = new CosmeticSnapshotBuilder();
        return b.build(List.of(redHead), 1);
    }
}