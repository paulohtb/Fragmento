package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.cosmetics.network.CosmeticSyncPublisher;
import com.pgalaxyp.fragmento.cosmetics.server.state.PlayerCosmeticState;
import com.pgalaxyp.fragmento.cosmetics.server.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.tiers.server.event.TierUpdatedEvent;
import com.pgalaxyp.fragmento.tiers.server.service.TierService;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CosmeticServiceImpl implements CosmeticService {

    private final ConcurrentHashMap<UUID, PlayerCosmeticState> states;
    private final CosmeticRegistry registry;
    private final CosmeticValidator validator;
    private final CosmeticSyncPublisher publisher;

    public CosmeticServiceImpl(
            CosmeticRegistry registry,
            TierService tierService,
            CosmeticValidator validator,
            CosmeticSyncPublisher publisher
    ) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.validator = Objects.requireNonNull(validator, "validator");
        this.publisher = Objects.requireNonNull(publisher, "publisher");
        this.states = new ConcurrentHashMap<>();

        Objects.requireNonNull(tierService, "tierService").registerListener(this::onTierUpdated);
    }

    @Override
    public CosmeticLoadoutSnapshot getSnapshot(UUID playerId) {
        PlayerCosmeticState s = states.getOrDefault(playerId, PlayerCosmeticState.EMPTY);
        return s.snapshot();
    }

    @Override
    public boolean equipBase(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        if (!validator.validateBase(playerId, slot, cosmeticId).success()) {
            return false;
        }

        states.compute(playerId, (id, prev) -> {
            PlayerCosmeticState cur = prev == null ? PlayerCosmeticState.EMPTY : prev;
            CosmeticLoadout nextBase = cur.base().with(slot, cosmeticId);
            PlayerCosmeticState next =
                    new PlayerCosmeticState(nextBase, cur.forced(), cur.version() + 1L);
            publisher.publish(id, next.snapshot());
            return next;
        });

        return true;
    }

    @Override
    public boolean unequipBase(UUID playerId, CosmeticSlot slot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");

        states.computeIfPresent(playerId, (id, cur) -> {
            CosmeticLoadout nextBase = cur.base().without(slot);
            PlayerCosmeticState next =
                    new PlayerCosmeticState(nextBase, cur.forced(), cur.version() + 1L);
            publisher.publish(id, next.snapshot());
            return next;
        });

        return true;
    }

    @Override
    public void setForced(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        states.compute(playerId, (id, prev) -> {
            PlayerCosmeticState cur = prev == null ? PlayerCosmeticState.EMPTY : prev;
            CosmeticLoadout nextForced = cur.forced().with(slot, cosmeticId);
            PlayerCosmeticState next =
                    new PlayerCosmeticState(cur.base(), nextForced, cur.version() + 1L);
            publisher.publish(id, next.snapshot());
            return next;
        });
    }

    @Override
    public void clearForced(UUID playerId, CosmeticSlot slot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");

        states.computeIfPresent(playerId, (id, cur) -> {
            CosmeticLoadout nextForced = cur.forced().without(slot);
            PlayerCosmeticState next =
                    new PlayerCosmeticState(cur.base(), nextForced, cur.version() + 1L);
            publisher.publish(id, next.snapshot());
            return next;
        });
    }

    private void onTierUpdated(TierUpdatedEvent ev) {
        if (ev == null) return;

        states.computeIfPresent(ev.playerId(), (id, state) -> {
            PlayerCosmeticState next = state.revalidate(registry, ev.tier());
            publisher.publish(id, next.snapshot());
            return next;
        });
    }
}