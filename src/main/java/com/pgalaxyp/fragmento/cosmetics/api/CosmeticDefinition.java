package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.Objects;

public final class CosmeticDefinition {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final CosmeticId id;
    private final CosmeticTypeId type;
    private final CosmeticSlot slot;
    private final CosmeticTier requiredTier;
    private final int priority;
    private final boolean visibleToSelf;
    private final CosmeticTransform transform;

    public CosmeticDefinition(CosmeticId id, CosmeticTypeId type, CosmeticSlot slot, CosmeticTier requiredTier, int priority, boolean visibleToSelf, CosmeticTransform transform) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = Objects.requireNonNull(type, "type");
        this.slot = Objects.requireNonNull(slot, "slot");
        this.requiredTier = Objects.requireNonNull(requiredTier, "requiredTier");
        this.priority = priority;
        this.visibleToSelf = visibleToSelf;
        this.transform = transform == null ? CosmeticTransform.IDENTITY : transform;
        LOGGER.debug("CosmeticDefinition criado, id {}, type {}, slot {}, tier {}, priority {}, self {}, transform {}", this.id, this.type, this.slot.name(), this.requiredTier.name(), Integer.valueOf(this.priority), Boolean.valueOf(this.visibleToSelf), this.transform);
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

    public CosmeticTransform transform() {
        return transform;
    }
}