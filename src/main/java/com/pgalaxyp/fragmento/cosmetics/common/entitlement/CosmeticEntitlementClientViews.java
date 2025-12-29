package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

import java.util.Objects;

public final class CosmeticEntitlementClientViews {

    private static final CosmeticEntitlementClientView FALLBACK = new Fallback();
    private static volatile CosmeticEntitlementClientView VIEW;

    private CosmeticEntitlementClientViews() {
    }

    public static CosmeticEntitlementClientView view() {
        CosmeticEntitlementClientView v = VIEW;
        return v == null ? FALLBACK : v;
    }

    public static void set(CosmeticEntitlementClientView view) {
        VIEW = Objects.requireNonNull(view, "view");
    }

    private static final class Fallback implements CosmeticEntitlementClientView {

        @Override
        public int level() {
            return 0;
        }

        @Override
        public long version() {
            return 0L;
        }

        @Override
        public String label() {
            return "Tier: ?";
        }
    }
}