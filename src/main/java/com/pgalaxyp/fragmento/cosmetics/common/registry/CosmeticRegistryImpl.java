package com.pgalaxyp.fragmento.cosmetics.common.registry;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import java.util.Objects;

public final class CosmeticRegistryImpl implements CosmeticRegistry {

    private volatile CosmeticDefinitionsSnapshot current = CosmeticDefinitionsSnapshot.EMPTY;

    @Override
    public CosmeticDefinition get(CosmeticId id) {
        if (id == null) {
            return null;
        }
        return current.byIdView().get(id);
    }

    @Override
    public CosmeticDefinitionsSnapshot snapshot() {
        return current;
    }

    public void setSnapshot(CosmeticDefinitionsSnapshot snapshot) {
        this.current = Objects.requireNonNull(snapshot, "snapshot");
    }
}