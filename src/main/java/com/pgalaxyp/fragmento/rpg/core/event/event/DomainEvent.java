package com.pgalaxyp.fragmento.rpg.core.event.event;

public sealed interface DomainEvent permits AuditEvent, VisualEvent {}