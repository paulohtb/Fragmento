package com.pgalaxyp.fragmento.cosmetic.client.state;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientCosmetics {

    private static final ConcurrentHashMap<UUID, PlayerStore> STORES = new ConcurrentHashMap<>();
    private static volatile boolean SHOW_OTHERS = true;

    private ClientCosmetics() {}

    public static boolean showOthers() {
        return SHOW_OTHERS;
    }

    public static void setShowOthers(boolean showOthers) {
        SHOW_OTHERS = showOthers;
    }

    public static void clearAll() {
        STORES.clear();
    }

    public static void remove(UUID playerId) {
        if (playerId != null) {
            STORES.remove(playerId);
        }
    }

    public static int catalogVersion(UUID playerId) {
        PlayerStore s = STORES.get(playerId);
        return s == null ? 0 : s.catalogVersion;
    }

    public static long rosterVersion(UUID playerId) {
        PlayerStore s = STORES.get(playerId);
        return s == null ? 0L : s.rosterVersion;
    }

    public static CosmeticId equipped(UUID playerId, CosmeticSlot slot) {
        PlayerStore s = STORES.get(playerId);
        if (s == null || slot == null) return null;
        return s.equipped.get(slot);
    }

    public static List<CosmeticEntry> entriesBySlot(UUID playerId, CosmeticSlot slot) {
        PlayerStore s = STORES.get(playerId);
        if (s == null || slot == null) return List.of();

        ArrayList<CosmeticEntry> out = new ArrayList<>();
        for (CosmeticEntry e : s.byId.values()) {
            if (e != null && e.slot() == slot) {
                out.add(e);
            }
        }

        out.sort(Comparator
                .comparingInt((CosmeticEntry e) -> e.info().sort())
                .thenComparing(e -> e.id().value())
        );

        return List.copyOf(out);
    }

    public static void applyFull(UUID playerId, int catalogVersion, long rosterVersion, List<CosmeticEntry> entries) {
        if (playerId == null) return;

        PlayerStore s = STORES.computeIfAbsent(playerId, k -> new PlayerStore());

        if (catalogVersion < s.catalogVersion) return;
        if (catalogVersion > s.catalogVersion) {
            s.byId.clear();
            s.equipped.clear();
            s.catalogVersion = catalogVersion;
            s.rosterVersion = 0L;
        }

        if (rosterVersion < s.rosterVersion) return;

        s.rosterVersion = rosterVersion;
        s.byId.clear();
        s.equipped.clear();

        if (entries != null) {
            for (CosmeticEntry e : entries) {
                if (e == null || e.info() == null || e.id() == null) continue;
                s.byId.put(e.id(), e);
                if (e.equipped()) {
                    s.equipped.put(e.slot(), e.id());
                }
            }
        }
    }

    public static void applyDelta(UUID playerId, long rosterVersion, CosmeticEntry entry) {
        if (playerId == null || entry == null || entry.id() == null) return;

        PlayerStore s = STORES.computeIfAbsent(playerId, k -> new PlayerStore());
        if (rosterVersion < s.rosterVersion) return;

        s.rosterVersion = rosterVersion;

        CosmeticEntry prev = s.byId.get(entry.id());
        if (prev != null && entry.version() < prev.version()) return;

        if (entry.equipped()) {
            CosmeticId prevEquipped = s.equipped.get(entry.slot());
            if (prevEquipped != null && !Objects.equals(prevEquipped, entry.id())) {
                CosmeticEntry old = s.byId.get(prevEquipped);
                if (old != null) {
                    s.byId.put(prevEquipped, new CosmeticEntry(old.info(), false, old.version()));
                }
            }
            s.equipped.put(entry.slot(), entry.id());
        } else {
            CosmeticId cur = s.equipped.get(entry.slot());
            if (Objects.equals(cur, entry.id())) {
                s.equipped.remove(entry.slot());
            }
        }

        s.byId.put(entry.id(), entry);
    }

    private static final class PlayerStore {
        final ConcurrentHashMap<CosmeticId, CosmeticEntry> byId = new ConcurrentHashMap<>();
        final EnumMap<CosmeticSlot, CosmeticId> equipped = new EnumMap<>(CosmeticSlot.class);
        volatile int catalogVersion;
        volatile long rosterVersion;

        PlayerStore() {
            catalogVersion = 0;
            rosterVersion = 0L;
        }
    }
}