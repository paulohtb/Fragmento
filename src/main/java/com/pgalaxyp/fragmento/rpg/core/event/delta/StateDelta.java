package com.pgalaxyp.fragmento.rpg.core.event.delta;

public sealed interface StateDelta
        permits ComboStarted, ComboAdvanced, ComboEnded, DamageApplied {
}