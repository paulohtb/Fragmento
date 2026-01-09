package com.pgalaxyp.fragmento.rpg.core.event.delta;

public sealed interface StateDelta
        permits ActorSpawned, ComboStarted, ComboAdvanced, ComboEnded, DamageApplied {
}