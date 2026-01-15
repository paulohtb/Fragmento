package com.pgalaxyp.fragmento.combat.core.events.event;

public sealed interface DomainEvent permits AuditEvent, VisualEvent {}