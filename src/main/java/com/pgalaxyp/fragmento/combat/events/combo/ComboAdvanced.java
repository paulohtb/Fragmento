package com.pgalaxyp.fragmento.combat.events.combo;

import com.pgalaxyp.fragmento.combat.combo.api.ComboInput;
import com.pgalaxyp.fragmento.combat.combo.api.ComboSnapshot;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ComboAdvanced(ActorId actorId, ComboSnapshot snapshot, ComboInput input) implements DomainEvent {
    public ComboAdvanced {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(snapshot);
        Objects.requireNonNull(input);
    }
}
