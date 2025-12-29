package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.cosmetics.server.network.CosmeticSyncPublisher;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.LevelAccessPolicy;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementUpdatedEvent;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import com.pgalaxyp.fragmento.cosmetics.common.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.server.state.PlayerCosmeticState;
import com.pgalaxyp.fragmento.cosmetics.server.state.SlotCosmetic;
import java.util.EnumMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class CosmeticServiceImpl implements CosmeticService {

    private final ConcurrentHashMap<UUID, PlayerCosmeticState> states;
    private final CosmeticRegistry registry;
    private final CosmeticValidator validator;
    private final CosmeticSyncPublisher publisher;
    private final LevelAccessPolicy accessPolicy;

    public CosmeticServiceImpl(
            CosmeticRegistry registry,
            PlayerEntitlementService entitlements,
            CosmeticValidator validator,
            CosmeticSyncPublisher publisher,
            LevelAccessPolicy accessPolicy
    ) {
        this.registry = Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(entitlements, "entitlements");
        this.validator = Objects.requireNonNull(validator, "validator");
        this.publisher = Objects.requireNonNull(publisher, "publisher");
        this.accessPolicy = Objects.requireNonNull(accessPolicy, "accessPolicy");
        this.states = new ConcurrentHashMap<>();
        entitlements.registerListener(new EntitlementUpdatedListener(this));
    }

    @Override
    public CosmeticLoadoutSnapshot getSnapshot(UUID playerId) {
        PlayerCosmeticState s = states.get(playerId);
        if (s == null) {
            s = PlayerCosmeticState.EMPTY;
        }
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

        applyUpdate(playerId, new EquipBaseOp(slot, cosmeticId), true);
        return true;
    }

    @Override
    public boolean unequipBase(UUID playerId, CosmeticSlot slot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");

        applyUpdate(playerId, new UnequipBaseOp(slot), false);
        return true;
    }

    @Override
    public void setForced(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        if (!validator.validateForced(playerId, slot, cosmeticId).success()) {
            return;
        }

        applyUpdate(playerId, new SetForcedOp(slot, cosmeticId), true);
    }

    @Override
    public void clearForced(UUID playerId, CosmeticSlot slot) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(slot, "slot");

        applyUpdate(playerId, new ClearForcedOp(slot), false);
    }

    private void onEntitlementUpdated(PlayerEntitlementUpdatedEvent ev) {
        if (ev == null) {
            return;
        }
        applyUpdate(ev.playerId(), new EntitlementUpdatedOp(ev.level()), false);
    }

    private void applyUpdate(UUID playerId, UpdateOp op, boolean createIfAbsent) {
        if (playerId == null || op == null) {
            return;
        }

        while (true) {
            PlayerCosmeticState cur = states.get(playerId);
            if (cur == null) {
                if (!createIfAbsent) {
                    return;
                }
                cur = PlayerCosmeticState.EMPTY;
            }

            UpdateResult res = op.apply(cur, registry, accessPolicy);
            if (res == null) {
                return;
            }

            PlayerCosmeticState next = res.state;
            boolean swapped;

            if (states.containsKey(playerId)) {
                swapped = states.replace(playerId, cur, next);
            } else {
                swapped = states.putIfAbsent(playerId, next) == null;
            }

            if (swapped) {
                if (res.publish) {
                    publisher.publish(playerId, next.snapshot());
                }
                return;
            }
        }
    }

    private static boolean sameEffective(CosmeticLoadout a, CosmeticLoadout b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.view().equals(b.view());
    }

    private interface UpdateOp {
        UpdateResult apply(PlayerCosmeticState cur, CosmeticRegistry registry, LevelAccessPolicy accessPolicy);
    }

    private record UpdateResult(PlayerCosmeticState state, boolean publish) {}

    private record EquipBaseOp(CosmeticSlot slot, CosmeticId cosmeticId) implements UpdateOp {

        @Override
        public UpdateResult apply(PlayerCosmeticState cur, CosmeticRegistry registry, LevelAccessPolicy accessPolicy) {
            CosmeticLoadout beforeEff = cur.resolveEffective();

            EnumMap<CosmeticSlot, SlotCosmetic> nextSlots = cur.slotsCopy();
            SlotCosmetic sc = nextSlots.get(slot);
            if (sc == null) {
                sc = new SlotCosmetic(null, null);
            }
            sc = sc.withBase(cosmeticId);
            if (sc.isEmpty()) {
                nextSlots.remove(slot);
            } else {
                nextSlots.put(slot, sc);
            }

            PlayerCosmeticState mid = new PlayerCosmeticState(nextSlots, cur.version());
            CosmeticLoadout afterEff = mid.resolveEffective();

            if (sameEffective(beforeEff, afterEff)) {
                return new UpdateResult(mid, false);
            }

            long nextVer = cur.version() + 1L;
            PlayerCosmeticState next = new PlayerCosmeticState(nextSlots, nextVer);
            return new UpdateResult(next, true);
        }
    }

    private record UnequipBaseOp(CosmeticSlot slot) implements UpdateOp {

        @Override
        public UpdateResult apply(PlayerCosmeticState cur, CosmeticRegistry registry, LevelAccessPolicy accessPolicy) {
            EnumMap<CosmeticSlot, SlotCosmetic> nextSlots = cur.slotsCopy();
            SlotCosmetic sc0 = nextSlots.get(slot);
            if (sc0 == null) {
                return new UpdateResult(cur, false);
            }

            CosmeticLoadout beforeEff = cur.resolveEffective();

            SlotCosmetic sc = sc0.withoutBase();
            if (sc.isEmpty()) {
                nextSlots.remove(slot);
            } else {
                nextSlots.put(slot, sc);
            }

            PlayerCosmeticState mid = new PlayerCosmeticState(nextSlots, cur.version());
            CosmeticLoadout afterEff = mid.resolveEffective();

            if (sameEffective(beforeEff, afterEff)) {
                return new UpdateResult(mid, false);
            }

            long nextVer = cur.version() + 1L;
            PlayerCosmeticState next = new PlayerCosmeticState(nextSlots, nextVer);
            return new UpdateResult(next, true);
        }
    }

    private record SetForcedOp(CosmeticSlot slot, CosmeticId cosmeticId) implements UpdateOp {

        @Override
        public UpdateResult apply(PlayerCosmeticState cur, CosmeticRegistry registry, LevelAccessPolicy accessPolicy) {
            CosmeticLoadout beforeEff = cur.resolveEffective();

            EnumMap<CosmeticSlot, SlotCosmetic> nextSlots = cur.slotsCopy();
            SlotCosmetic sc = nextSlots.get(slot);
            if (sc == null) {
                sc = new SlotCosmetic(null, null);
            }
            sc = sc.withForced(cosmeticId);
            if (sc.isEmpty()) {
                nextSlots.remove(slot);
            } else {
                nextSlots.put(slot, sc);
            }

            PlayerCosmeticState mid = new PlayerCosmeticState(nextSlots, cur.version());
            CosmeticLoadout afterEff = mid.resolveEffective();

            if (sameEffective(beforeEff, afterEff)) {
                return new UpdateResult(mid, false);
            }

            long nextVer = cur.version() + 1L;
            PlayerCosmeticState next = new PlayerCosmeticState(nextSlots, nextVer);
            return new UpdateResult(next, true);
        }
    }

    private record ClearForcedOp(CosmeticSlot slot) implements UpdateOp {

        @Override
        public UpdateResult apply(PlayerCosmeticState cur, CosmeticRegistry registry, LevelAccessPolicy accessPolicy) {
            EnumMap<CosmeticSlot, SlotCosmetic> nextSlots = cur.slotsCopy();
            SlotCosmetic sc0 = nextSlots.get(slot);
            if (sc0 == null) {
                return new UpdateResult(cur, false);
            }

            CosmeticLoadout beforeEff = cur.resolveEffective();

            SlotCosmetic sc = sc0.withoutForced();
            if (sc.isEmpty()) {
                nextSlots.remove(slot);
            } else {
                nextSlots.put(slot, sc);
            }

            PlayerCosmeticState mid = new PlayerCosmeticState(nextSlots, cur.version());
            CosmeticLoadout afterEff = mid.resolveEffective();

            if (sameEffective(beforeEff, afterEff)) {
                return new UpdateResult(mid, false);
            }

            long nextVer = cur.version() + 1L;
            PlayerCosmeticState next = new PlayerCosmeticState(nextSlots, nextVer);
            return new UpdateResult(next, true);
        }
    }

    private record EntitlementUpdatedOp(int level) implements UpdateOp {

        @Override
        public UpdateResult apply(PlayerCosmeticState cur, CosmeticRegistry registry, LevelAccessPolicy accessPolicy) {
            CosmeticLoadout beforeEff = cur.resolveEffective();

            PlayerCosmeticState mid = cur.revalidate(registry, level, accessPolicy);
            CosmeticLoadout afterEff = mid.resolveEffective();

            if (sameEffective(beforeEff, afterEff)) {
                return new UpdateResult(mid, false);
            }

            long nextVer = cur.version() + 1L;
            PlayerCosmeticState next = new PlayerCosmeticState(mid.slotsCopy(), nextVer);
            return new UpdateResult(next, true);
        }
    }

    private record EntitlementUpdatedListener(CosmeticServiceImpl owner) implements Consumer<PlayerEntitlementUpdatedEvent> {

        @Override
        public void accept(PlayerEntitlementUpdatedEvent ev) {
            owner.onEntitlementUpdated(ev);
        }
    }
}