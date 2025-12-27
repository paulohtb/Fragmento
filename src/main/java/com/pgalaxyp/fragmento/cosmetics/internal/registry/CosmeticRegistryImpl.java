package com.pgalaxyp.fragmento.cosmetics.internal.registry;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.internal.snapshot.CosmeticDefinitionsSnapshot;
import java.util.Objects;

public final class CosmeticRegistryImpl implements CosmeticRegistry {

    private volatile CosmeticDefinitionsSnapshot current =
            CosmeticDefinitionsSnapshot.EMPTY;

    @Override
    public CosmeticDefinition get(CosmeticId id) {
        if (id == null) return null;
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