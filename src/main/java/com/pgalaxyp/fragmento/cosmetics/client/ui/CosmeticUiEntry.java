package com.pgalaxyp.fragmento.cosmetics.client.ui;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;

public record CosmeticUiEntry(
        CosmeticDefinition definition,
        boolean equipped,
        boolean allowed
) {}