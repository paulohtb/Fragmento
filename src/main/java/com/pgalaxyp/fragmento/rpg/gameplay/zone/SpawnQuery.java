package com.pgalaxyp.fragmento.rpg.gameplay.zone;

import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.SpawnRule;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.Target;

public record SpawnQuery(long actorId, Target target, SpawnRule rule, SpawnSide lastSide, double distance) {}