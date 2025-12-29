package com.pgalaxyp.fragmento.cosmetics.common.validation;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementSnapshot;
import java.util.Objects;
import java.util.UUID;

public final class EntitlementServicePlayerLevelResolver implements PlayerLevelResolver {

    private final PlayerEntitlementService entitlements;

    public EntitlementServicePlayerLevelResolver(PlayerEntitlementService entitlements) {
        this.entitlements = Objects.requireNonNull(entitlements, "entitlements");
    }

    @Override
    public int resolveLevel(UUID playerId) {
        if (playerId == null) {
            return 0;
        }
        long now = System.currentTimeMillis();
        PlayerEntitlementSnapshot snap = entitlements.snapshot(playerId, now);
        if (snap == null) {
            return 0;
        }
        int lvl = snap.level();
        return Math.max(0, lvl);
    }
}