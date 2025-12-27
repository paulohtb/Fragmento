package com.pgalaxyp.fragmento.cosmetics.client;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadoutSnapshot;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CosmeticsClientState {

    private static final ConcurrentHashMap<UUID, CosmeticLoadoutSnapshot> SNAPSHOTS =
            new ConcurrentHashMap<>();

    private static volatile boolean SHOW_OTHERS = true;

    private CosmeticsClientState() {}

    public static boolean showOthers() {
        return SHOW_OTHERS;
    }

    public static void setShowOthers(boolean showOthers) {
        SHOW_OTHERS = showOthers;
    }

    public static CosmeticLoadout getEffective(UUID playerId) {
        CosmeticLoadoutSnapshot snap = SNAPSHOTS.get(playerId);
        if (snap == null) {
            return CosmeticLoadout.EMPTY;
        }
        return snap.loadout();
    }

    public static long getVersion(UUID playerId) {
        CosmeticLoadoutSnapshot snap = SNAPSHOTS.get(playerId);
        if (snap == null) {
            return 0L;
        }
        return snap.version();
    }

    public static void put(UUID playerId, CosmeticLoadoutSnapshot snapshot) {
        CosmeticLoadoutSnapshot prev = SNAPSHOTS.get(playerId);
        if (prev != null && snapshot.version() < prev.version()) {
            return;
        }
        SNAPSHOTS.put(playerId, snapshot);
    }

    public static void clear(UUID playerId) {
        SNAPSHOTS.remove(playerId);
    }

    public static void clearAll() {
        SNAPSHOTS.clear();
    }
}