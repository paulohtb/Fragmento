package com.pgalaxyp.fragmento.rpg.core.state.delta;

public sealed interface StateDelta permits ActionStateDelta, ComboStateDelta {
    long actorId();
}