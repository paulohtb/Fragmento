package com.pgalaxyp.fragmento.cosmetics.client.state;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;

public final class CosmeticsClientEntitlements {

    private static volatile CosmeticEntitlementClientView VIEW;

    private CosmeticsClientEntitlements() {}

    public static void set(CosmeticEntitlementClientView view) {
        VIEW = view;
    }

    public static CosmeticEntitlementClientView view() {
        return VIEW;
    }
}
