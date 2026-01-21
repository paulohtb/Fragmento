package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.events.ability.AbilityRequested;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.intent.AbilityUseIntent;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.core.state.GameState;

public final class AbilityInputSystem implements FrameSystem {
    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (IntentEnvelope env : bus.intents(IntentEnvelope.class)) {
            if (env.intent() instanceof AbilityUseIntent(AbilityId abilityId, WeaponId weaponId)) {
                bus.publish(new AbilityRequested(env.actorId(), abilityId, weaponId));
            }
        }
    }
}