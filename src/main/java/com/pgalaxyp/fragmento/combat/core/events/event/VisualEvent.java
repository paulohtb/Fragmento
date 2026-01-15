package com.pgalaxyp.fragmento.combat.core.events.event;

public sealed interface VisualEvent extends DomainEvent permits HomingMagicVisualEvent {
    long frameId();
    int localIndex();
}