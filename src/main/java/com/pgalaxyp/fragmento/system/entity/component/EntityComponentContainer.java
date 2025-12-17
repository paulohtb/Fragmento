package com.pgalaxyp.fragmento.system.entity.component;

import java.util.ArrayList;
import java.util.List;

public final class EntityComponentContainer {

    private final List<EntityComponent> components = new ArrayList<>(4);

    public void add(EntityComponent component) {
        if (component != null) {
            components.add(component);
        }
    }

    public List<EntityComponent> components() {
        return components;
    }
}