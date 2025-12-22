package com.pgalaxyp.fragmento.cosmetics.client;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticCatalogEntry;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class CosmeticsClientState {

    private static final Map<UUID, Entry> COSMETICS = new ConcurrentHashMap<>();
    private static final AtomicReferenceArray<BaseSetResult> LAST_SET_BASE = new AtomicReferenceArray<>(CosmeticSlot.values().length);

    private static volatile CosmeticTier LOCAL_TIER = CosmeticTier.TIER_0;
    private static volatile long LOCAL_TIER_VERSION = 0L;

    private static volatile int CATALOG_DATA_VERSION = 0;
    private static volatile List<CosmeticCatalogEntry> UNLOCKED = new ArrayList<>();
    private static volatile int CATALOG_TIERS = 0;
    private static volatile int CATALOG_SLOTS = 0;
    private static volatile int[] LOCKED_COUNTS = new int[0];
    private static volatile Map<ResourceLocation, CosmeticCatalogEntry> UNLOCKED_BY_ID = new HashMap<>();

    private static volatile boolean SHOW_OTHERS = true;

    private CosmeticsClientState() {
    }

    public static void setEffective(UUID playerId, CosmeticLoadout loadout, long version) {
        if (playerId == null) return;
        if (loadout == null) return;
        COSMETICS.put(playerId, new Entry(loadout, version));
    }

    public static CosmeticLoadout getEffective(UUID playerId) {
        if (playerId == null) return CosmeticLoadout.EMPTY;
        Entry e = COSMETICS.get(playerId);
        if (e == null) return CosmeticLoadout.EMPTY;
        return e.loadout;
    }

    public static void clear(UUID playerId) {
        if (playerId == null) return;
        COSMETICS.remove(playerId);
    }

    public static void clearAll() {
        COSMETICS.clear();
        for (int i = 0; i < LAST_SET_BASE.length(); i++) {
            LAST_SET_BASE.set(i, null);
        }
        UNLOCKED = new ArrayList<>();
        UNLOCKED_BY_ID = new HashMap<>();
        LOCKED_COUNTS = new int[0];
        CATALOG_DATA_VERSION = 0;
        CATALOG_TIERS = 0;
        CATALOG_SLOTS = 0;
        LOCAL_TIER = CosmeticTier.TIER_0;
        LOCAL_TIER_VERSION = 0L;
        SHOW_OTHERS = true;
    }

    public static void setLocalTier(CosmeticTier tier, long version) {
        CosmeticTier t = tier == null ? CosmeticTier.TIER_0 : tier;
        LOCAL_TIER = t;
        LOCAL_TIER_VERSION = version;
    }

    public static CosmeticTier localTier() {
        return LOCAL_TIER;
    }

    public static long localTierVersion() {
        return LOCAL_TIER_VERSION;
    }

    public static void setCatalog(int playerTierLevel, int dataVersion, List<CosmeticCatalogEntry> unlocked, int tiers, int slots, int[] lockedCounts) {
        LOCAL_TIER = CosmeticTier.fromLevel(playerTierLevel);
        CATALOG_DATA_VERSION = dataVersion;
        List<CosmeticCatalogEntry> list = unlocked == null ? new ArrayList<>() : unlocked;
        UNLOCKED = list;
        CATALOG_TIERS = tiers;
        CATALOG_SLOTS = slots;
        LOCKED_COUNTS = lockedCounts == null ? new int[0] : lockedCounts;

        HashMap<ResourceLocation, CosmeticCatalogEntry> map = new HashMap<>(Math.max(16, list.size() * 2));
        for (int i = 0; i < list.size(); i++) {
            CosmeticCatalogEntry e = list.get(i);
            if (e == null) continue;
            map.put(e.id(), e);
        }
        UNLOCKED_BY_ID = map;
    }

    public static CosmeticCatalogEntry getUnlockedEntry(ResourceLocation cosmeticId) {
        if (cosmeticId == null) return null;
        return UNLOCKED_BY_ID.get(cosmeticId);
    }

    public static int catalogDataVersion() {
        return CATALOG_DATA_VERSION;
    }

    public static List<CosmeticCatalogEntry> unlockedView() {
        return UNLOCKED;
    }

    public static int lockedCount(int tierLevel, int slotOrdinal) {
        int tiers = CATALOG_TIERS;
        int slots = CATALOG_SLOTS;
        if (tierLevel < 0) return 0;
        if (slotOrdinal < 0) return 0;
        if (tierLevel >= tiers) return 0;
        if (slotOrdinal >= slots) return 0;
        int idx = Math.addExact(Math.multiplyExact(tierLevel, slots), slotOrdinal);
        int[] counts = LOCKED_COUNTS;
        if (idx < 0) return 0;
        if (idx >= counts.length) return 0;
        return counts[idx];
    }

    public static Map<CosmeticSlot, List<CosmeticCatalogEntry>> unlockedBySlot() {
        EnumMap<CosmeticSlot, List<CosmeticCatalogEntry>> out = new EnumMap<>(CosmeticSlot.class);
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            out.put(slot, new ArrayList<>());
        }
        List<CosmeticCatalogEntry> list = UNLOCKED;
        for (int i = 0; i < list.size(); i++) {
            CosmeticCatalogEntry e = list.get(i);
            if (e == null) continue;
            CosmeticSlot slot = e.slot();
            if (slot == null) continue;
            List<CosmeticCatalogEntry> dst = out.get(slot);
            if (dst != null) dst.add(e);
        }
        return out;
    }

    public static void setLastSetBaseResult(int slotOrdinal, boolean success, String errorCode, long version) {
        if (slotOrdinal < 0) return;
        if (slotOrdinal >= LAST_SET_BASE.length()) return;
        String code = errorCode == null ? "invalid" : errorCode;
        LAST_SET_BASE.set(slotOrdinal, new BaseSetResult(success, code, version));
    }

    public static BaseSetResult lastSetBaseResult(int slotOrdinal) {
        if (slotOrdinal < 0) return null;
        if (slotOrdinal >= LAST_SET_BASE.length()) return null;
        return LAST_SET_BASE.get(slotOrdinal);
    }

    public static boolean showOthers() {
        return SHOW_OTHERS;
    }

    public static void setShowOthers(boolean value) {
        SHOW_OTHERS = value;
    }

    private static final class Entry {
        private final CosmeticLoadout loadout;
        private final long version;

        private Entry(CosmeticLoadout loadout, long version) {
            this.loadout = loadout;
            this.version = version;
        }
    }

    public static final class BaseSetResult {
        private final boolean success;
        private final String errorCode;
        private final long version;

        private BaseSetResult(boolean success, String errorCode, long version) {
            this.success = success;
            this.errorCode = errorCode;
            this.version = version;
        }

        public boolean success() {
            return success;
        }

        public String errorCode() {
            return errorCode;
        }

        public long version() {
            return version;
        }
    }
}