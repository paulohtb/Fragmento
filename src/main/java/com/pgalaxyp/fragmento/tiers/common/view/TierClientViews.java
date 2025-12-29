package com.pgalaxyp.fragmento.tiers.common.view;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import java.util.Objects;

public final class TierClientViews {

    private static final TierClientView FALLBACK = new Fallback();
    private static volatile TierClientView VIEW;

    private TierClientViews() {}

    public static TierClientView view() {
        TierClientView v = VIEW;
        return v == null ? FALLBACK : v;
    }

    public static void set(TierClientView view) {
        VIEW = Objects.requireNonNull(view, "view");
    }

    private static final class Fallback implements TierClientView {

        @Override
        public Tier tier() {
            return Tier.inactive();
        }

        @Override
        public long version() {
            return 0L;
        }
    }
}