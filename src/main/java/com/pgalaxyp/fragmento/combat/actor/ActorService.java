package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.*;

public interface ActorService {
    List<StateDelta> onJoin(ActorId actorId, GameState state, SpawnDefaults defaults);
}
