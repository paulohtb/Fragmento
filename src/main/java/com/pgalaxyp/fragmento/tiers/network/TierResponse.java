package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import java.util.Objects;
import java.util.Optional;

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

    public Optional<Tier> tier() {
        return Optional.ofNullable(tier);
    }

    public Optional<TierError> error() {
        return Optional.ofNullable(error);
    }
}