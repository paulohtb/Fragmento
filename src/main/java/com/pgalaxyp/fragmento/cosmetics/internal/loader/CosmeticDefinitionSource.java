package com.pgalaxyp.fragmento.cosmetics.internal.loader;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import java.util.List;

public interface CosmeticDefinitionSource {

    List<CosmeticDefinition> loadAll();
}