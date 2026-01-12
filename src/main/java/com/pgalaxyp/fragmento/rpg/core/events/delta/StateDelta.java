package com.pgalaxyp.fragmento.rpg.core.events.delta;

public sealed interface StateDelta
        permits ActorSpawned, ComboStarted, ComboAdvanced, ComboEnded, DamageApplied {
}