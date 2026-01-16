package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class ClassCatalog {

    private final Set<ClassId> classes;

    public ClassCatalog(Set<ClassId> classes) {
        this.classes = Set.copyOf(classes);
    }

    public boolean exists(ClassId id) {
        return classes.contains(id);
    }
}