package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import org.slf4j.Logger;

public final class CosmeticDefinition {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final CosmeticId id;
    private final CosmeticTypeId type;
    private final CosmeticSlot slot;
    private final CosmeticTier requiredTier;
    private final int priority;
    private final boolean visibleToSelf;

    public CosmeticDefinition(CosmeticId id, CosmeticTypeId type, CosmeticSlot slot, CosmeticTier requiredTier, int priority, boolean visibleToSelf) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = Objects.requireNonNull(type, "type");
        this.slot = Objects.requireNonNull(slot, "slot");
        this.requiredTier = Objects.requireNonNull(requiredTier, "requiredTier");
        this.priority = priority;
        this.visibleToSelf = visibleToSelf;
        LOGGER.debug("CosmeticDefinition criado, id {}, type {}, slot {}, tier {}, priority {}, self {}", this.id, this.type, this.slot.name(), this.requiredTier.name(), Integer.valueOf(this.priority), Boolean.valueOf(this.visibleToSelf));
    }

    public CosmeticId id() {
        return id;
    }

    public CosmeticTypeId type() {
        return type;
    }

    public CosmeticSlot slot() {
        return slot;
    }

    public CosmeticTier requiredTier() {
        return requiredTier;
    }

    public int priority() {
        return priority;
    }

    public boolean visibleToSelf() {
        return visibleToSelf;
    }
}