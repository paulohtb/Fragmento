package com.pgalaxyp.fragmento.rpg.combo.state;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public interface ComboTracker {

    Optional<ComboState> get(ActorId actorId);

    void put(ActorId actorId, ComboState state);
    void clear(ActorId actorId);
}