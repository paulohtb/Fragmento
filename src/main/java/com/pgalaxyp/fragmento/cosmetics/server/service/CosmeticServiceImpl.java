package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.common.sync.VersionedStateSyncService;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.ProgressionBasedCosmeticEntitlementCore;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.cosmetics.common.sync.CosmeticStateView;
import com.pgalaxyp.fragmento.cosmetics.common.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.server.state.PlayerCosmeticState;
import com.pgalaxyp.fragmento.cosmetics.server.state.SlotCosmetic;
import java.util.EnumMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CosmeticServiceImpl implements CosmeticService {

    private final ConcurrentHashMap<UUID, PlayerCosmeticState> states =
            new ConcurrentHashMap<>();

    private final CosmeticValidator validator;
    private final CosmeticRegistry registry;
    private final ProgressionBasedCosmeticEntitlementCore entitlements;

    private volatile VersionedStateSyncService<CosmeticStateView> sync;

    public CosmeticServiceImpl(
            CosmeticValidator validator,
            CosmeticRegistry registry,
            ProgressionBasedCosmeticEntitlementCore entitlements
    ) {
        this.validator = validator;
        this.registry = registry;
        this.entitlements = entitlements;
    }

    public void bindSync(VersionedStateSyncService<CosmeticStateView> sync) {
        this.sync = sync;
    }

    @Override
    public CosmeticLoadoutSnapshot getSnapshot(UUID playerId) {
        PlayerCosmeticState s = states.get(playerId);
        return s == null
                ? PlayerCosmeticState.EMPTY.snapshot()
                : s.snapshot();
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

    public void onProgressionChanged(UUID playerId) {
        states.computeIfPresent(playerId, (k, cur) -> {
            PlayerCosmeticState next =
                    cur.revalidate(registry, playerId, entitlements);

            if (next != cur) {
                VersionedStateSyncService<CosmeticStateView> s = sync;
                if (s != null) {
                    s.onUpdated(playerId, next.version());
                }
            }
            return next;
        });
    }

    private void apply(UUID playerId, CosmeticSlot slot, CosmeticId id) {
        states.compute(playerId, (k, cur) -> {
            PlayerCosmeticState base =
                    cur == null ? PlayerCosmeticState.EMPTY : cur;

            EnumMap<CosmeticSlot, SlotCosmetic> next =
                    base.slotsCopy();

            if (id == null) {
                next.remove(slot);
            } else {
                next.put(slot, new SlotCosmetic(id));
            }

            PlayerCosmeticState updated =
                    new PlayerCosmeticState(next, base.version() + 1L);

            VersionedStateSyncService<CosmeticStateView> s = sync;
            if (s != null) {
                s.onUpdated(playerId, updated.version());
            }

            return updated;
        });
    }
}