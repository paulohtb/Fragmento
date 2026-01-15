package com.pgalaxyp.fragmento.combat.combo.state;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public interface ComboTracker {

    Optional<ComboState> get(ActorId actorId);

    void put(ActorId actorId, ComboState state);
    void clear(ActorId actorId);
}