package com.pgalaxyp.fragmento.cosmetics.internal.builtin;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.internal.loader.CosmeticSnapshotBuilder;
import com.pgalaxyp.fragmento.cosmetics.internal.snapshot.CosmeticDefinitionsSnapshot;
import com.pgalaxyp.fragmento.cosmetics.shared.CosmeticsBuiltinDefinitions;
import java.util.List;

public final class BuiltinCosmetics {

    private static final CosmeticDefinitionsSnapshot SNAPSHOT = build();

    private BuiltinCosmetics() {}

    public static CosmeticDefinitionsSnapshot snapshot() {
        return SNAPSHOT;
    }

    private static CosmeticDefinitionsSnapshot build() {
        List<CosmeticDefinition> defs = CosmeticsBuiltinDefinitions.loadAll();
        CosmeticSnapshotBuilder b = new CosmeticSnapshotBuilder();
        return b.build(defs, 1);
    }
}