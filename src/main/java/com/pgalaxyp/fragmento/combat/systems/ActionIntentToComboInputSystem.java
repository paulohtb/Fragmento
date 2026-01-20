package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.combo.api.ComboId;
import com.pgalaxyp.fragmento.combat.combo.api.ComboInput;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.combo.ComboInputReceived;
import com.pgalaxyp.fragmento.combat.flow.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import java.util.*;

public final class ActionIntentToComboInputSystem implements FrameSystem {
    private final GameContent content;

    public ActionIntentToComboInputSystem(GameContent content) {
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (IntentEnvelope env : bus.intents(IntentEnvelope.class)) {
            if (!(env.intent() instanceof PerformActionIntent pai)) continue;

            var actorOpt = state.findActor(env.actorId());
            if (actorOpt.isEmpty()) continue;

            WeaponId weapon = actorOpt.get().equippedWeaponId().orElse(null);
            if (weapon == null) continue;

            var baseOpt = content.combos().baseFor(weapon);
            if (baseOpt.isEmpty()) continue;

            ComboId comboId = baseOpt.get().comboId();
            ComboInput input = pai.input();
            bus.publish(new ComboInputReceived(env.actorId(), comboId, input));
        }
    }
}