package com.pgalaxyp.fragmento.rpg.core.events.event;

public record AuditEvent(
        String message
) implements DomainEvent {
    public AuditEvent {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}