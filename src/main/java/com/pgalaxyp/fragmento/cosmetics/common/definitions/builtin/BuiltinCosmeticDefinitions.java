package com.pgalaxyp.fragmento.cosmetics.common.definitions.builtin;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticTypeId;
import java.util.ArrayList;
import java.util.List;

public final class BuiltinCosmeticDefinitions {

    private BuiltinCosmeticDefinitions() {}

    public static List<CosmeticDefinition> loadAll() {
        ArrayList<CosmeticDefinition> out = new ArrayList<>(1);

        out.add(new CosmeticDefinition(
                CosmeticId.of("fragmento:red_cube_head"),
                CosmeticTypeId.of("builtin_cube"),
                CosmeticSlot.HEAD,
                3,
                0,
                true
        ));

        return out;
    }
}