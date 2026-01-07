package com.pgalaxyp.fragmento.rpg.core.domain.event;

public sealed interface DomainEvent
        permits ActionStarted, ComboAdvanced, EffectTriggered, TargetingRequested {
}