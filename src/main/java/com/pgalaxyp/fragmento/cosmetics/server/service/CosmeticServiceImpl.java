package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.cosmetics.common.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.server.network.CosmeticSyncPublisher;
import com.pgalaxyp.fragmento.cosmetics.server.state.PlayerCosmeticState;
import com.pgalaxyp.fragmento.cosmetics.server.state.SlotCosmetic;
import com.pgalaxyp.fragmento.tiers.common.service.TierUpdatedEvent;
import java.util.EnumMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class CosmeticServiceImpl implements CosmeticService, Consumer<TierUpdatedEvent> {

    private final ConcurrentHashMap<UUID, PlayerCosmeticState> states = new ConcurrentHashMap<>();
    private final CosmeticValidator validator;
    private final CosmeticSyncPublisher publisher;
    private final CosmeticRegistry registry;
    private final CosmeticEntitlementService entitlements;

    public CosmeticServiceImpl(
            CosmeticValidator validator,
            CosmeticSyncPublisher publisher,
            CosmeticRegistry registry,
            CosmeticEntitlementService entitlements
    ) {
        this.validator = validator;
        this.publisher = publisher;
        this.registry = registry;
        this.entitlements = entitlements;
    }

    @Override
    public CosmeticLoadoutSnapshot getSnapshot(UUID playerId) {
        PlayerCosmeticState s = states.get(playerId);
        return s == null ? PlayerCosmeticState.EMPTY.snapshot() : s.snapshot();
    }

    @Override
    public boolean equipBase(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId) {
        if (!validator.validate(playerId, slot, cosmeticId)) {
            return false;
        }
        apply(playerId, slot, cosmeticId);
        return true;
    }

    @Override
    public boolean unequipBase(UUID playerId, CosmeticSlot slot) {
        apply(playerId, slot, null);
        return true;
    }

    private void apply(UUID playerId, CosmeticSlot slot, CosmeticId id) {
        states.compute(playerId, (k, cur) -> {
            PlayerCosmeticState base = cur == null ? PlayerCosmeticState.EMPTY : cur;
            EnumMap<CosmeticSlot, SlotCosmetic> next = base.slotsCopy();

            if (id == null) {
                next.remove(slot);
            } else {
                next.put(slot, new SlotCosmetic(id));
            }

            PlayerCosmeticState updated =
                    new PlayerCosmeticState(next, base.version() + 1L);

            publisher.publish(playerId, updated.snapshot());
            return updated;
        });
    }

    @Override
    public void accept(TierUpdatedEvent ev) {
        UUID playerId = ev.playerId();
        states.computeIfPresent(playerId, (k, cur) -> {
            PlayerCosmeticState next =
                    cur.revalidate(registry, playerId, entitlements);
            if (next != cur) {
                publisher.publish(playerId, next.snapshot());
            }
            return next;
        });
    }
}