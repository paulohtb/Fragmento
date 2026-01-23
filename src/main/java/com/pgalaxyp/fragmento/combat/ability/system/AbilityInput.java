package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.ability.event.AbilityRequested;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.intent.AbilityUseIntent;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.flow.FrameContext;
import com.pgalaxyp.fragmento.combat.core.state.GameState;

public final class AbilityInput implements FrameSystem {
    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (IntentEnvelope env : bus.intents(IntentEnvelope.class)) {
            if (env.intent() instanceof AbilityUseIntent(AbilityId abilityId, WeaponId weaponId)) {
                bus.publish(new AbilityRequested(env.actorId(), abilityId, weaponId));
            }
        }
    }
}