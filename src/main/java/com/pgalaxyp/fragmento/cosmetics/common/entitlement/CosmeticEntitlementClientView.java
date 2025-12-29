package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;

public interface CosmeticEntitlementClientView {

    boolean allowed(CosmeticDefinition def);

    long version();

    String label();
}