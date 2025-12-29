package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

public record PlayerEntitlementSnapshot(
        int level,
        long fetchedAtMillis,
        long expiresAtMillis,
        long version
) {
    public boolean expired(long nowMillis) {
        return nowMillis >= expiresAtMillis;
    }
}