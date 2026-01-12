package com.pgalaxyp.fragmento.rpg.core.events.event;

public sealed interface DomainEvent permits AuditEvent, VisualEvent {}