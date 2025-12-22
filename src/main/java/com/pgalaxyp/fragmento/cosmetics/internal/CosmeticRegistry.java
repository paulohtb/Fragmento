package com.pgalaxyp.fragmento.cosmetics.internal;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;

public interface CosmeticRegistry {

    CosmeticDefinition getDefinition(CosmeticId id);

    CosmeticDefinitionsSnapshot snapshot();
}