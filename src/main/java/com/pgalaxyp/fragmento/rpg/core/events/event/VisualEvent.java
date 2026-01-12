package com.pgalaxyp.fragmento.rpg.core.events.event;

public sealed interface VisualEvent extends DomainEvent permits HomingMagicVisualEvent {
    long frameId();
    int localIndex();
}