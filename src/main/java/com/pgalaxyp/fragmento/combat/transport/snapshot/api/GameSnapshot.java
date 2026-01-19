package com.pgalaxyp.fragmento.combat.transport.snapshot.api;

import com.pgalaxyp.fragmento.combat.ability.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import java.util.*;

public record GameSnapshot(
        FrameContext frame,
        NavigableMap<ActorId, ActorState> actors,
        List<AbilityInstanceView> activeAbilities
) {}