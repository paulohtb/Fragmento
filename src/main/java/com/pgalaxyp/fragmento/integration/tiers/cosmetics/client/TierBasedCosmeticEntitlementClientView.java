package com.pgalaxyp.fragmento.integration.tiers.cosmetics.client;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.tiers.client.state.TierClientState;

public final class TierBasedCosmeticEntitlementClientView
        implements CosmeticEntitlementClientView {

    @Override
    public boolean allowed(CosmeticDefinition def) {
        if (def == null) {
            return false;
        }

        int required = def.requiredLevel();
        if (required <= 0) {
            return true;
        }

        return TierClientState.level() >= required;
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