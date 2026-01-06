package com.pgalaxyp.fragmento.rpg.core.rule.zone;

import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record SpawnResult(boolean success, Vec3 position, SpawnSide usedSide) {}