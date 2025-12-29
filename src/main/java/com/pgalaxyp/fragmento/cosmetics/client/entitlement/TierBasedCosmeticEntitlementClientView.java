package com.pgalaxyp.fragmento.cosmetics.client.entitlement;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.tiers.common.display.TierDisplays;
import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierStatus;
import com.pgalaxyp.fragmento.tiers.common.view.TierClientState;

public final class TierBasedCosmeticEntitlementClientView implements CosmeticEntitlementClientView {

    @Override
    public int level() {
        Tier t = TierClientState.get();
        if (t == null) {
            return 0;
        }
        if (t.status() != TierStatus.ACTIVE) {
            return 0;
        }
        return Math.max(0, t.level().value());
    }

    @Override
    public long version() {
        return TierClientState.version();
    }

    @Override
    public String label() {
        Tier t = TierClientState.get();
        return TierDisplays.DEFAULT.label(t);
    }
}