package com.pgalaxyp.fragmento.cosmetics.server.state;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.LevelAccessPolicy;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class PlayerCosmeticState {

    public static final PlayerCosmeticState EMPTY = new PlayerCosmeticState(new EnumMap<>(CosmeticSlot.class), 0L);

    private final EnumMap<CosmeticSlot, SlotCosmetic> slots;
    private final long version;

    public PlayerCosmeticState(EnumMap<CosmeticSlot, SlotCosmetic> slots, long version) {
        Objects.requireNonNull(slots, "slots");
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
            if (sc == null) {
                continue;
            }
            CosmeticId id = sc.effective();
            if (id != null) {
                out.put(e.getKey(), id);
            }
        }
        return new CosmeticLoadout(out);
    }

    public PlayerCosmeticState revalidate(CosmeticRegistry registry, int playerLevel, LevelAccessPolicy accessPolicy) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(accessPolicy, "accessPolicy");

        int lvl = Math.max(0, playerLevel);
        EnumMap<CosmeticSlot, SlotCosmetic> next = new EnumMap<>(CosmeticSlot.class);

        CosmeticSlot[] slotsArr = CosmeticSlot.values();
        for (CosmeticSlot slot : slotsArr) {
            SlotCosmetic cur = slots.get(slot);
            if (cur == null || cur.isEmpty()) {
                continue;
            }

            CosmeticId base = cur.base();
            CosmeticId forced = cur.forced();

            CosmeticId baseOk = validateOne(registry, lvl, accessPolicy, slot, base);
            CosmeticId forcedOk = validateOne(registry, lvl, accessPolicy, slot, forced);

            SlotCosmetic rebuilt = new SlotCosmetic(baseOk, forcedOk);
            if (!rebuilt.isEmpty()) {
                next.put(slot, rebuilt);
            }
        }

        return new PlayerCosmeticState(next, version);
    }

    private static CosmeticId validateOne(
            CosmeticRegistry registry,
            int playerLevel,
            LevelAccessPolicy accessPolicy,
            CosmeticSlot slot,
            CosmeticId id
    ) {
        if (id == null) {
            return null;
        }

        CosmeticDefinition def = registry.get(id);
        if (def == null) {
            return null;
        }
        if (def.slot() != slot) {
            return null;
        }

        if (accessPolicy.allowed(playerLevel, def.requiredLevel())) {
            return id;
        }
        return null;
    }
}