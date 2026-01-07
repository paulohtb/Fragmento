package com.pgalaxyp.fragmento.rpg.core.domain.event;

public sealed interface DomainEvent
        permits TargetingRequested, EffectTriggered {
}