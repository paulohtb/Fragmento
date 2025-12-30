package com.pgalaxyp.fragmento.cosmetics.server.service;

import com.pgalaxyp.fragmento.bridge.progression.PlayerProgressionView;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticCatalog;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticInfo;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CosmeticsServiceImpl implements CosmeticsService {

    private final CosmeticCatalog catalog;
    private final PlayerProgressionView progression;
    private final CosmeticsPublisher publisher;

    private final ConcurrentHashMap<UUID, PlayerCosmetics> players = new ConcurrentHashMap<>();

    public CosmeticsServiceImpl(
            CosmeticCatalog catalog,
            PlayerProgressionView progression,
            CosmeticsPublisher publisher
    ) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.progression = Objects.requireNonNull(progression, "progression");
        this.publisher = Objects.requireNonNull(publisher, "publisher");
    }

    @Override
    public List<CosmeticEntry> snapshot(UUID playerId) {
        if (playerId == null) return List.of();
        PlayerCosmetics st = players.computeIfAbsent(playerId, k -> new PlayerCosmetics());

        ArrayList<CosmeticEntry> out = new ArrayList<>(catalog.all().size());
        for (CosmeticInfo info : catalog.all()) {
            if (info == null) continue;
            CosmeticId id = info.id();
            boolean equipped = Objects.equals(st.equipped(info.slot()), id);
            long v = st.cosmeticVersion(id);
            out.add(new CosmeticEntry(info, equipped, v));
        }

        out.sort(Comparator.comparingInt((CosmeticEntry a) -> a.info().sort()).thenComparing(a -> a.id().value()));

        return List.copyOf(out);
    }

    @Override
    public boolean equip(UUID playerId, CosmeticId cosmeticId) {
        if (playerId == null || cosmeticId == null) return false;

        CosmeticInfo info = catalog.get(cosmeticId);
        if (info == null) return false;

        int tier = Math.max(0, progression.level(playerId));
        if (tier < info.requiredTier()) return false;

        PlayerCosmetics st = players.computeIfAbsent(playerId, k -> new PlayerCosmetics());

        Publish publish = equipInternal(st, playerId, cosmeticId, info);
        if (!publish.changed) return true;

        if (publish.prevEntry != null) {
            publisher.sendDelta(playerId, publish.rosterVersion, publish.prevEntry, true);
        }
        if (publish.newEntry != null) {
            publisher.sendDelta(playerId, publish.rosterVersion, publish.newEntry, true);
        }

        return true;
    }

    @Override
    public boolean unequip(UUID playerId, CosmeticSlot slot) {
        if (playerId == null || slot == null) return false;

        PlayerCosmetics st = players.computeIfAbsent(playerId, k -> new PlayerCosmetics());

        Publish publish = unequipInternal(st, playerId, slot);
        if (!publish.changed) return true;

        if (publish.prevEntry != null) {
            publisher.sendDelta(playerId, publish.rosterVersion, publish.prevEntry, true);
        }

        return true;
    }

    @Override
    public void onTierChanged(UUID playerId) {
        if (playerId == null) return;

        PlayerCosmetics st = players.get(playerId);
        if (st == null) return;

        int tier = Math.max(0, progression.level(playerId));

        ArrayList<CosmeticEntry> removals = new ArrayList<>();
        long rosterV;

        synchronized (st) {
            EnumMap<CosmeticSlot, CosmeticId> equippedNow = st.equippedCopy();
            ArrayList<CosmeticId> toRemove = new ArrayList<>();

            for (var e : equippedNow.entrySet()) {
                CosmeticSlot slot = e.getKey();
                CosmeticId id = e.getValue();
                if (id == null) continue;

                CosmeticInfo info = catalog.get(id);
                if (info == null) {
                    toRemove.add(id);
                    st.setEquipped(slot, null);
                    continue;
                }

                if (tier < info.requiredTier()) {
                    toRemove.add(id);
                    st.setEquipped(slot, null);
                }
            }

            if (toRemove.isEmpty()) {
                return;
            }

            rosterV = st.bumpRoster();

            for (CosmeticId id : toRemove) {
                CosmeticInfo info = catalog.get(id);
                if (info == null) continue;
                long v = st.bumpCosmetic(id);
                removals.add(new CosmeticEntry(info, false, v));
            }
        }

        for (CosmeticEntry removed : removals) {
            publisher.sendDelta(playerId, rosterV, removed, true);
        }
    }

    @Override
    public void onLogin(UUID playerId) {
        if (playerId == null) return;
        PlayerCosmetics st = players.computeIfAbsent(playerId, k -> new PlayerCosmetics());

        long rosterV = st.bumpRoster();
        publisher.sendFull(playerId, playerId, catalog.dataVersion(), rosterV, snapshot(playerId));
    }

    @Override
    public void onLogout(UUID playerId) {
        if (playerId == null) return;
        players.remove(playerId);
    }

    @Override
    public void onStartTracking(UUID ownerId, UUID trackerId) {
        if (ownerId == null || trackerId == null) return;
        PlayerCosmetics st = players.computeIfAbsent(ownerId, k -> new PlayerCosmetics());
        long rosterV = st.rosterVersion();
        publisher.sendFull(ownerId, trackerId, catalog.dataVersion(), rosterV, snapshot(ownerId));
    }

    private Publish equipInternal(PlayerCosmetics st, UUID playerId, CosmeticId cosmeticId, CosmeticInfo info) {
        synchronized (st) {
            CosmeticSlot slot = info.slot();
            CosmeticId prev = st.equipped(slot);
            if (Objects.equals(prev, cosmeticId)) {
                return Publish.noChange();
            }

            long rosterV = st.bumpRoster();

            CosmeticEntry prevEntry = null;
            if (prev != null) {
                st.setEquipped(slot, null);
                long vPrev = st.bumpCosmetic(prev);
                CosmeticInfo prevInfo = catalog.get(prev);
                if (prevInfo != null) {
                    prevEntry = new CosmeticEntry(prevInfo, false, vPrev);
                }
            }

            st.setEquipped(slot, cosmeticId);
            long vNew = st.bumpCosmetic(cosmeticId);
            CosmeticEntry newEntry = new CosmeticEntry(info, true, vNew);

            return new Publish(true, rosterV, prevEntry, newEntry);
        }
    }

    private Publish unequipInternal(PlayerCosmetics st, UUID playerId, CosmeticSlot slot) {
        synchronized (st) {
            CosmeticId prev = st.equipped(slot);
            if (prev == null) {
                return Publish.noChange();
            }

            st.setEquipped(slot, null);

            long rosterV = st.bumpRoster();
            long vPrev = st.bumpCosmetic(prev);

            CosmeticInfo prevInfo = catalog.get(prev);
            CosmeticEntry prevEntry = prevInfo == null ? null : new CosmeticEntry(prevInfo, false, vPrev);

            return new Publish(true, rosterV, prevEntry, null);
        }
    }

    private record Publish(boolean changed, long rosterVersion, CosmeticEntry prevEntry, CosmeticEntry newEntry) {

        static Publish noChange() {
                return new Publish(false, 0L, null, null);
            }
        }
}