package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;

public record ActionLockState(
        ActionKind actionKind,
        SkillId skillId,
        Time endsAt,
        Time itemSwapLockedUntil
) {

    public static ActionLockState idle() {
        Time zero = Time.ofTicks(0L);
        return new ActionLockState(ActionKind.NONE, null, zero, zero);
    }

    public boolean isActive(Time now) {
        return now != null && now.isBefore(endsAt);
    }

    public boolean itemSwapLocked(Time now) {
        return now != null && now.isBefore(itemSwapLockedUntil);
    }

    public boolean blocksVanillaInteraction(Time now) {
        return isActive(now) && actionKind == ActionKind.COMBO_STEP;
    }
}