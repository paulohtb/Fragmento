package com.pgalaxyp.fragmento.cosmetics.server.state;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.ProgressionBasedCosmeticEntitlementCore;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerCosmeticState {

    public static final PlayerCosmeticState EMPTY =
            new PlayerCosmeticState(new EnumMap<>(CosmeticSlot.class), 0L);

    private final EnumMap<CosmeticSlot, SlotCosmetic> slots;
    private final long version;

    public PlayerCosmeticState(EnumMap<CosmeticSlot, SlotCosmetic> slots, long version) {
        EnumMap<CosmeticSlot, SlotCosmetic> copy = new EnumMap<>(CosmeticSlot.class);
        copy.putAll(slots);
        this.slots = copy;
        this.version = version;
    }

    public long version() {
        return version;
    }

    public EnumMap<CosmeticSlot, SlotCosmetic> slotsCopy() {
        EnumMap<CosmeticSlot, SlotCosmetic> copy = new EnumMap<>(CosmeticSlot.class);
        copy.putAll(slots);
        return copy;
    }

    public CosmeticLoadoutSnapshot snapshot() {
        return CosmeticLoadoutSnapshot.of(resolveEffective(), version);
    }

    public CosmeticLoadout resolveEffective() {
        EnumMap<CosmeticSlot, CosmeticId> out = new EnumMap<>(CosmeticSlot.class);
        for (Map.Entry<CosmeticSlot, SlotCosmetic> e : slots.entrySet()) {
            SlotCosmetic sc = e.getValue();
            if (sc == null) continue;
            CosmeticId id = sc.effective();
            if (id != null) out.put(e.getKey(), id);
        }
        return new CosmeticLoadout(out);
    }

    public PlayerCosmeticState revalidate(
            CosmeticRegistry registry,
            UUID playerId,
            ProgressionBasedCosmeticEntitlementCore entitlements
    ) {
        EnumMap<CosmeticSlot, SlotCosmetic> next =
                new EnumMap<>(CosmeticSlot.class);

        for (Map.Entry<CosmeticSlot, SlotCosmetic> e : slots.entrySet()) {
            SlotCosmetic sc = e.getValue();
            if (sc == null || sc.isEmpty()) continue;

            CosmeticId id = sc.base();
            CosmeticDefinition def = registry.get(id);
            if (def == null) continue;
            if (def.slot() != e.getKey()) continue;
            if (!entitlements.allowed(playerId, def)) continue;

            next.put(e.getKey(), sc);
        }

        if (next.equals(slots)) {
            return this;
        }

        return new PlayerCosmeticState(next, version + 1L);
    }
}