package com.pgalaxyp.fragmento.combat.events.combo;

import com.pgalaxyp.fragmento.combat.combo.api.ComboId;
import com.pgalaxyp.fragmento.combat.combo.api.ComboResetReason;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ComboReset(ActorId actorId, ComboId comboId, ComboResetReason reason) implements DomainEvent {
    public ComboReset {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(reason);
    }
}
