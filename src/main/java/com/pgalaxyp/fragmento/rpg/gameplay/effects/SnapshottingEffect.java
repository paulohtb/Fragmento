package com.pgalaxyp.fragmento.rpg.gameplay.effects;

import com.pgalaxyp.fragmento.rpg.gameplay.state.EffectSnapshot;

public interface SnapshottingEffect {
    long ownerActorId();
    EffectSnapshot snapshot();
}