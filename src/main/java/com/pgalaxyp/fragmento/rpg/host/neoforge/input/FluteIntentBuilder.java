package com.pgalaxyp.fragmento.rpg.host.neoforge.input;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboStartIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.util.Optional;

public final class FluteIntentBuilder {

    public static IntentEnvelope buildClientIntent(ActorId actorId, Optional<GameSnapshot> lastSnapshot) {
        if (actorId == null || lastSnapshot == null) {
            throw new IllegalArgumentException();
        }

        if (lastSnapshot.isPresent()) {
            var s = lastSnapshot.get().actors().get(actorId);
            if (s != null && s.combo().isPresent()) {
                return IntentEnvelope.of(actorId, new ComboAdvanceIntent(s.combo().get().actionId()));
            }
        }

        return IntentEnvelope.of(actorId, new ComboStartIntent());
    }

    private FluteIntentBuilder() {}
}