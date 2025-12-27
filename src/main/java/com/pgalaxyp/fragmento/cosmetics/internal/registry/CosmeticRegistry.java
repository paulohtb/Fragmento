package com.pgalaxyp.fragmento.cosmetics.internal.registry;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.internal.snapshot.CosmeticDefinitionsSnapshot;

public interface CosmeticRegistry {

    CosmeticDefinition get(CosmeticId id);

    CosmeticDefinitionsSnapshot snapshot();
}