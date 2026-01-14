package com.pgalaxyp.fragmento.rpg.core.events.delta;

public sealed interface StateDelta permits ActorSpawned, ComboAdvanced, ComboEnded, ComboStarted, DamageApplied {}