package com.pgalaxyp.fragmento.combat.events.combo;

import com.pgalaxyp.fragmento.combat.combo.api.ComboId;
import com.pgalaxyp.fragmento.combat.combo.api.ComboInput;
import com.pgalaxyp.fragmento.combat.combo.api.ComboRejectReason;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ComboRejected(ActorId actorId, ComboId comboId, ComboInput input, ComboRejectReason reason) implements DomainEvent {
    public ComboRejected {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(input);
        Objects.requireNonNull(reason);
    }
}
