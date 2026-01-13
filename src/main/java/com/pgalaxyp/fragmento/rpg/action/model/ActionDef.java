package com.pgalaxyp.fragmento.rpg.action.model;

import com.pgalaxyp.fragmento.rpg.action.id.*;
import java.util.*;

public record ActionDef(ActionId id, ActionKind kind, ActionSpec spec) {

    public ActionDef {
        Objects.requireNonNull(id);
        Objects.requireNonNull(kind);
        Objects.requireNonNull(spec);
    }
}