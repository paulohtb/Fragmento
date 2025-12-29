package com.pgalaxyp.fragmento.cosmetics.client.entitlement;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.tiers.common.view.TierClientState;

public final class TierBasedCosmeticEntitlementClientView
        implements CosmeticEntitlementClientView {

    @Override
    public int level() {
        return TierClientState.level();
    }

    @Override
    public long version() {
        return TierClientState.version();
    }

    @Override
    public String label() {
        return "Tier: " + TierClientState.level();
    }
}