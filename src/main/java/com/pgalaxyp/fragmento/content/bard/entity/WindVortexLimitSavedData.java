package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardVortexConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class WindVortexLimitSavedData extends SavedData {

    private static final String NAME = "fragmento_wind_vortex_limits";
    private static final int STALE_RESET_TICKS = 300;

    private final Map<UUID, OwnerState> owners = new HashMap<>();

    public static WindVortexLimitSavedData get(net.minecraft.server.level.ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        WindVortexLimitSavedData::new,
                        WindVortexLimitSavedData::load
                ),
                NAME
        );
    }

    public boolean canSpawn(UUID ownerId, WindVortexLimitService.VortexTier tier, long now) {
        if (ownerId == null || tier == null) return false;
        evictIfStale(now);

        OwnerState owner = owners.get(ownerId);
        int current = owner != null ? owner.getCount(tier) : 0;
        int limit = limitFor(tier);

        return current < limit;
    }

    public boolean tryReserve(UUID ownerId, WindVortexLimitService.VortexTier tier, long now) {
        if (ownerId == null || tier == null) return false;
        evictIfStale(now);

        OwnerState owner = owners.get(ownerId);
        if (owner == null) {
            owner = new OwnerState(now);
            owners.put(ownerId, owner);
        } else {
            owner.lastTouchedGameTime = now;
        }

        int current = owner.getCount(tier);
        int limit = limitFor(tier);

        if (current >= limit) return false;

        owner.increment(tier);
        setDirty();
        return true;
    }

    public void release(UUID ownerId, WindVortexLimitService.VortexTier tier, long now) {
        if (ownerId == null || tier == null) return;
        evictIfStale(now);

        OwnerState owner = owners.get(ownerId);
        if (owner == null) return;

        owner.lastTouchedGameTime = now;
        owner.decrement(tier);

        if (owner.isEmpty()) {
            owners.remove(ownerId);
        }

        setDirty();
    }

    public void clearOwner(UUID ownerId) {
        if (ownerId == null) return;
        owners.remove(ownerId);
        setDirty();
    }

    public void clearAll() {
        owners.clear();
        setDirty();
    }

    private void evictIfStale(long now) {
        if (owners.isEmpty()) return;

        boolean changed = false;
        var it = owners.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            OwnerState s = e.getValue();
            long age = now - s.lastTouchedGameTime;
            if (age > STALE_RESET_TICKS) {
                it.remove();
                changed = true;
            }
        }

        if (changed) setDirty();
    }

    private static int limitFor(WindVortexLimitService.VortexTier tier) {
        return switch (tier) {
            case MINOR -> BardVortexConstants.MINOR_MAX_ACTIVE_PER_OWNER;
            case MEDIUM -> BardVortexConstants.MEDIUM_MAX_ACTIVE_PER_OWNER;
        };
    }

    public static WindVortexLimitSavedData load(CompoundTag root, HolderLookup.Provider provider) {
        WindVortexLimitSavedData data = new WindVortexLimitSavedData();

        ListTag list = root.getList("Owners", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);

            if (!t.hasUUID("Id")) continue;
            UUID id = t.getUUID("Id");

            OwnerState owner = new OwnerState(t.getLong("Touched"));

            CompoundTag counts = t.getCompound("Counts");
            for (WindVortexLimitService.VortexTier tier : WindVortexLimitService.VortexTier.values()) {
                if (counts.contains(tier.name(), Tag.TAG_INT)) {
                    int v = counts.getInt(tier.name());
                    if (v > 0) owner.counts.put(tier, v);
                }
            }

            if (!owner.isEmpty()) {
                data.owners.put(id, owner);
            }
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag root, HolderLookup.Provider provider) {
        ListTag list = new ListTag();

        for (var e : owners.entrySet()) {
            CompoundTag t = new CompoundTag();
            t.putUUID("Id", e.getKey());

            OwnerState s = e.getValue();
            t.putLong("Touched", s.lastTouchedGameTime);

            CompoundTag counts = new CompoundTag();
            for (var c : s.counts.entrySet()) {
                counts.putInt(c.getKey().name(), c.getValue());
            }
            t.put("Counts", counts);

            list.add(t);
        }

        root.put("Owners", list);
        return root;
    }

    private static final class OwnerState {
        private final EnumMap<WindVortexLimitService.VortexTier, Integer> counts =
                new EnumMap<>(WindVortexLimitService.VortexTier.class);

        private long lastTouchedGameTime;

        private OwnerState(long now) {
            this.lastTouchedGameTime = now;
        }

        private int getCount(WindVortexLimitService.VortexTier tier) {
            Integer v = counts.get(tier);
            return v != null ? v : 0;
        }

        private void increment(WindVortexLimitService.VortexTier tier) {
            counts.put(tier, getCount(tier) + 1);
        }

        private void decrement(WindVortexLimitService.VortexTier tier) {
            int v = getCount(tier) - 1;
            if (v <= 0) {
                counts.remove(tier);
                return;
            }
            counts.put(tier, v);
        }

        private boolean isEmpty() {
            return counts.isEmpty();
        }
    }
}
