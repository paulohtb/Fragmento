package com.pgalaxyp.fragmento.cosmetics.common.registry;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;

public interface CosmeticRegistry {
    CosmeticDefinition get(CosmeticId id);
    CosmeticDefinitionsSnapshot snapshot();
}