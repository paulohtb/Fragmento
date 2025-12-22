package com.pgalaxyp.fragmento.cosmetics.internal;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import java.util.Objects;
import org.slf4j.Logger;

public final class CosmeticRegistryImpl implements CosmeticRegistry {

    private static final Logger LOGGER = LogUtils.getLogger();

    private volatile CosmeticDefinitionsSnapshot current;

    public CosmeticRegistryImpl() {
        this.current = CosmeticDefinitionsSnapshot.EMPTY;
        LOGGER.info("CosmeticRegistryImpl iniciado");
    }

    @Override
    public CosmeticDefinition getDefinition(CosmeticId id) {
        if (id == null) return null;
        CosmeticDefinition def = current.byIdView().get(id);
        if (def == null) {
            LOGGER.debug("CosmeticRegistryImpl getDefinition miss, {}", id);
        }
        return def;
    }

    @Override
    public CosmeticDefinitionsSnapshot snapshot() {
        return current;
    }

    public void setSnapshot(CosmeticDefinitionsSnapshot snapshot) {
        this.current = Objects.requireNonNull(snapshot, "snapshot");
        LOGGER.info("CosmeticRegistryImpl setSnapshot, ids {}, slots {}, version {}", Integer.valueOf(this.current.byIdView().size()), Integer.valueOf(this.current.bySlotView().size()), Integer.valueOf(this.current.dataVersion()));
    }
}