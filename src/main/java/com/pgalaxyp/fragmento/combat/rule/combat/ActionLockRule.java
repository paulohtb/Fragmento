package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.action.ActionKind;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import com.pgalaxyp.fragmento.combat.state.runtime.ActionLockState;

public final class ActionLockRule {

    public ActionLockState lock(ActionKind kind, SkillId skillId, Duration duration, CombatTime now) {
        ActionKind safeKind = kind != null ? kind : ActionKind.NONE;
        CombatTime endsAt = now.plus(duration != null ? duration : Duration.ofTicks(0L));

        CombatTime swapLockUntil = CombatTime.ofTicks(0L);
        if (safeKind == ActionKind.CAST_FINISH) {
            swapLockUntil = endsAt;
        }

        return new ActionLockState(safeKind, skillId, endsAt, swapLockUntil);
    }
}