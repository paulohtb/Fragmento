package com.pgalaxyp.fragmento.cosmetics.server.bootstrap;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementUpdatedEvent;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class CosmeticsServerWiring {

    private static volatile PlayerEntitlementService ENTITLEMENTS;

    private CosmeticsServerWiring() {}

    public static void bindEntitlements(PlayerEntitlementService service) {
        ENTITLEMENTS = Objects.requireNonNull(service, "service");
    }

    public static PlayerEntitlementService entitlements() {
        PlayerEntitlementService s = ENTITLEMENTS;
        if (s == null) {
            s = NoopEntitlements.INSTANCE;
            ENTITLEMENTS = s;
        }
        return s;
    }

    private static final class NoopEntitlements implements PlayerEntitlementService {

        private static final NoopEntitlements INSTANCE = new NoopEntitlements();

        @Override
        public PlayerEntitlementSnapshot snapshot(UUID playerId, long nowMillis) {
            return new PlayerEntitlementSnapshot(0, nowMillis, nowMillis + 60000L, 0L);
        }

        @Override
        public void registerListener(Consumer<PlayerEntitlementUpdatedEvent> listener) {
        }
    }
}