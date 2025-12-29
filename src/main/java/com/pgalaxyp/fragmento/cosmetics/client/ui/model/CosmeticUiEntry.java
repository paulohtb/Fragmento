package com.pgalaxyp.fragmento.cosmetics.client.ui.model;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;

public record CosmeticUiEntry(
        CosmeticDefinition definition,
        boolean equipped,
        boolean allowed
) {}