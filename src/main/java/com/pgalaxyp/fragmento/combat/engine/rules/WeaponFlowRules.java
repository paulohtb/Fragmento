package com.pgalaxyp.fragmento.combat.engine.rules;

import com.pgalaxyp.fragmento.combat.domain.input.WeaponInput;
import com.pgalaxyp.fragmento.combat.state.session.WeaponSessionState;
import static com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType.*;

public final class WeaponFlowRules {

    public boolean canAcceptInput(WeaponSessionState session, WeaponInput input) {
        if (session == null || input == null) {
            return false;
        }

        if (input.type() == PRIMARY_ATTACK) {
            return session.action().canAcceptWeaponAction();
        }

        if (input.type() == SKILL_PRESS) {
            return session.action().canAcceptSkillAction();
        }

        return input.type() == SKILL_CANCEL;
    }
}