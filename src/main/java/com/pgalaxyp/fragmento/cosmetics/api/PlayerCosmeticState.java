package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;

public final class PlayerCosmeticState {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final CosmeticLoadout baseLoadout;
    private final CosmeticLoadout forcedLoadout;
    private final long version;

    public PlayerCosmeticState(CosmeticLoadout baseLoadout, CosmeticLoadout forcedLoadout, long version) {
        this.baseLoadout = Objects.requireNonNull(baseLoadout, "baseLoadout");
        this.forcedLoadout = Objects.requireNonNull(forcedLoadout, "forcedLoadout");
        this.version = version;
        LOGGER.debug("PlayerCosmeticState criado, base {}, forced {}, version {}", Integer.valueOf(this.baseLoadout.equippedView().size()), Integer.valueOf(this.forcedLoadout.equippedView().size()), Long.valueOf(this.version));
    }

    public static PlayerCosmeticState empty() {
        return new PlayerCosmeticState(CosmeticLoadout.EMPTY, CosmeticLoadout.EMPTY, 0L);
    }

    public CosmeticLoadout baseLoadout() {
        return baseLoadout;
    }

    public CosmeticLoadout forcedLoadout() {
        return forcedLoadout;
    }

    public long version() {
        return version;
    }

    public CosmeticLoadout resolveEffectiveLoadout() {
        Map<CosmeticSlot, CosmeticId> base = baseLoadout.equippedView();
        Map<CosmeticSlot, CosmeticId> forced = forcedLoadout.equippedView();
        EnumMap<CosmeticSlot, CosmeticId> out = new EnumMap<>(CosmeticSlot.class);
        for (Map.Entry<CosmeticSlot, CosmeticId> e : base.entrySet()) {
            out.put(e.getKey(), e.getValue());
        }
        for (Map.Entry<CosmeticSlot, CosmeticId> e : forced.entrySet()) {
            out.put(e.getKey(), e.getValue());
        }
        CosmeticLoadout effective = new CosmeticLoadout(out);
        LOGGER.debug("PlayerCosmeticState resolveEffectiveLoadout, slots {}", Integer.valueOf(effective.equippedView().size()));
        return effective;
    }

    public PlayerCosmeticState withBaseSet(CosmeticSlot slot, CosmeticId id) {
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(id, "id");
        PlayerCosmeticState next = new PlayerCosmeticState(baseLoadout.with(slot, id), forcedLoadout, version + 1L);
        LOGGER.info("PlayerCosmeticState withBaseSet, slot {}, id {}, version {}", slot.name(), id, Long.valueOf(next.version));
        return next;
    }

    public PlayerCosmeticState withBaseCleared(CosmeticSlot slot) {
        Objects.requireNonNull(slot, "slot");
        PlayerCosmeticState next = new PlayerCosmeticState(baseLoadout.without(slot), forcedLoadout, version + 1L);
        LOGGER.info("PlayerCosmeticState withBaseCleared, slot {}, version {}", slot.name(), Long.valueOf(next.version));
        return next;
    }

    public PlayerCosmeticState withForcedSet(CosmeticSlot slot, CosmeticId id) {
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(id, "id");
        PlayerCosmeticState next = new PlayerCosmeticState(baseLoadout, forcedLoadout.with(slot, id), version + 1L);
        LOGGER.info("PlayerCosmeticState withForcedSet, slot {}, id {}, version {}", slot.name(), id, Long.valueOf(next.version));
        return next;
    }

    public PlayerCosmeticState withForcedCleared(CosmeticSlot slot) {
        Objects.requireNonNull(slot, "slot");
        PlayerCosmeticState next = new PlayerCosmeticState(baseLoadout, forcedLoadout.without(slot), version + 1L);
        LOGGER.info("PlayerCosmeticState withForcedCleared, slot {}, version {}", slot.name(), Long.valueOf(next.version));
        return next;
    }

    public PlayerCosmeticState withForcedClearedAll() {
        if (forcedLoadout.isEmpty()) {
            LOGGER.debug("PlayerCosmeticState withForcedClearedAll ignorado, forced vazio");
            return this;
        }
        PlayerCosmeticState next = new PlayerCosmeticState(baseLoadout, CosmeticLoadout.EMPTY, version + 1L);
        LOGGER.info("PlayerCosmeticState withForcedClearedAll, version {}", Long.valueOf(next.version));
        return next;
    }
}