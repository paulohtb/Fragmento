package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboStartIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.input.api.InputContext;
import java.util.Optional;

public final class ComboInputSystem {

    public Optional<DomainIntent> onPrimaryAction(InputContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (context.actorId().isEmpty()) {
            return Optional.empty();
        }

        ActorId actorId = context.actorId().get();
        Optional<ActorState> st = context.snapshot().findActor(actorId);
        if (st.isEmpty()) {
            return Optional.empty();
        }

        ActorState a = st.get();
        if (a.combo().isPresent()) {
            return Optional.of(new ComboAdvanceIntent(a.combo().get().actionId()));
        }

        return Optional.of(new ComboStartIntent());
    }
}