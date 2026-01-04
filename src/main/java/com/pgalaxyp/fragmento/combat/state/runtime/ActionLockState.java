package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.action.ActionKind;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record ActionLockState(
        ActionKind actionKind,
        SkillId skillId,
        CombatTime endsAt,
        CombatTime itemSwapLockedUntil
) {

    public static ActionLockState idle() {
        CombatTime zero = CombatTime.ofTicks(0L);
        return new ActionLockState(ActionKind.NONE, null, zero, zero);
    }

    public boolean isActive(CombatTime now) {
        return now != null && now.isBefore(endsAt);
    }

    public boolean itemSwapLocked(CombatTime now) {
        return now != null && now.isBefore(itemSwapLockedUntil);
    }

    public boolean blocksVanillaInteraction(CombatTime now) {
        return isActive(now) && actionKind == ActionKind.COMBO_STEP;
    }
}