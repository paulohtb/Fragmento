package com.pgalaxyp.fragmento.tiers.server.http;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import java.util.Objects;

public final class TierResponse {

    private final Tier tier;
    private final TierError error;

    private TierResponse(Tier tier, TierError error) {
        this.tier = tier;
        this.error = error;
    }

    public static TierResponse success(Tier tier) {
        return new TierResponse(Objects.requireNonNull(tier, "tier"), null);
    }

    public static TierResponse error(TierError error) {
        return new TierResponse(null, Objects.requireNonNull(error, "error"));
    }

    public boolean success() {
        return tier != null;
    }

    public Tier tierOrNull() {
        return tier;
    }

    public TierError errorOrNull() {
        return error;
    }
}