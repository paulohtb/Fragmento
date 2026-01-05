package com.pgalaxyp.fragmento.rpg.gameplay.zone;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public record SpawnResult(boolean success, Vec3 position, SpawnSide usedSide) {}