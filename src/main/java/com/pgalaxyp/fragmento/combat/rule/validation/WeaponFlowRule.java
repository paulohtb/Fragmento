package com.pgalaxyp.fragmento.combat.rule.validation;

import com.pgalaxyp.fragmento.combat.domain.input.WeaponInput;
import com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType;
import com.pgalaxyp.fragmento.combat.state.session.WeaponSessionState;

public final class WeaponFlowRule {

    public boolean canAcceptInput(WeaponSessionState session, WeaponInput input) {
        if (session == null || input == null) {
            return false;
        }

        if (input.type() == WeaponInputType.PRIMARY_ATTACK) {
            return session.action().canAcceptWeaponAction();
        }

        if (input.type() == WeaponInputType.SKILL_PRESS) {
            return session.action().canAcceptSkillAction();
        }

        return input.type() == WeaponInputType.SKILL_CANCEL;
    }
}