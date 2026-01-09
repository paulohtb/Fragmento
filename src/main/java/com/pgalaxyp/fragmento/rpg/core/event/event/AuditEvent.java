package com.pgalaxyp.fragmento.rpg.core.event.event;

public record AuditEvent(
        String message
) implements DomainEvent {
    public AuditEvent {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}