package com.pgalaxyp.fragmento.combat.core.events.event;

public record AuditEvent(
        String message
) implements DomainEvent {
    public AuditEvent {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}