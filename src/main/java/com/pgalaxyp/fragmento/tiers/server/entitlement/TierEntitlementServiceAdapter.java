package com.pgalaxyp.fragmento.tiers.server.entitlement;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementUpdatedEvent;
import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import com.pgalaxyp.fragmento.tiers.common.service.TierSnapshot;
import com.pgalaxyp.fragmento.tiers.common.service.TierUpdatedEvent;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class TierEntitlementServiceAdapter implements PlayerEntitlementService {

    private final TierService tiers;
    private final CopyOnWriteArrayList<Consumer<PlayerEntitlementUpdatedEvent>> listeners;

    public TierEntitlementServiceAdapter(TierService tiers) {
        this.tiers = Objects.requireNonNull(tiers, "tiers");
        this.listeners = new CopyOnWriteArrayList<>();
        this.tiers.registerListener(this::onTierUpdated);
    }

    @Override
    public PlayerEntitlementSnapshot snapshot(UUID playerId, long nowMillis) {
        TierSnapshot snap = tiers.snapshot(playerId, nowMillis);
        if (snap == null) {
            return new PlayerEntitlementSnapshot(0, nowMillis, nowMillis, 0L);
        }

        int level = toLevel(snap.tier());

        return new PlayerEntitlementSnapshot(
                level,
                snap.fetchedAtMillis(),
                snap.expiresAtMillis(),
                snap.version()
        );
    }

    @Override
    public void registerListener(Consumer<PlayerEntitlementUpdatedEvent> listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    @Override
    public void invalidate(UUID playerId) {
        tiers.invalidate(playerId);
    }

    private void onTierUpdated(TierUpdatedEvent ev) {
        if (ev == null) {
            return;
        }

        int level = toLevel(ev.tier());
        PlayerEntitlementUpdatedEvent mapped =
                new PlayerEntitlementUpdatedEvent(ev.playerId(), level, ev.version());

        for (Consumer<PlayerEntitlementUpdatedEvent> listener : listeners) {
            listener.accept(mapped);
        }
    }

    private static int toLevel(Tier tier) {
        if (tier == null) {
            return 0;
        }
        if (!tier.active()) {
            return 0;
        }
        return tier.level().value();
    }
}