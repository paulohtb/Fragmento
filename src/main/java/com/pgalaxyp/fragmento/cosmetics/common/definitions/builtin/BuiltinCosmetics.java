package com.pgalaxyp.fragmento.cosmetics.common.definitions.builtin;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticDefinitionsSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticSnapshotBuilder;
import java.util.List;

public final class BuiltinCosmetics {

    private static final CosmeticDefinitionsSnapshot SNAPSHOT = build();

    private BuiltinCosmetics() {}

    public static CosmeticDefinitionsSnapshot snapshot() {
        return SNAPSHOT;
    }

    private static CosmeticDefinitionsSnapshot build() {
        List<CosmeticDefinition> defs = BuiltinCosmeticDefinitions.loadAll();
        CosmeticSnapshotBuilder b = new CosmeticSnapshotBuilder();
        return b.build(defs, 1);
    }
}