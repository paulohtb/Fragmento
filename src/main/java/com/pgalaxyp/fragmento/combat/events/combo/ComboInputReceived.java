package com.pgalaxyp.fragmento.combat.events.combo;

import com.pgalaxyp.fragmento.combat.combo.api.ComboId;
import com.pgalaxyp.fragmento.combat.combo.api.ComboInput;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ComboInputReceived(ActorId actorId, ComboId comboId, ComboInput input) implements DomainEvent {
    public ComboInputReceived {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(input);
    }
}
