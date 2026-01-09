package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

public record ComboState(
        ActionId actionId,
        WeaponId weaponId,
        int stepIndex,
        int stepsTotal,
        long lastStepFrameId
) {
    public ComboState {
        if (actionId == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        if (stepsTotal <= 0) {
            throw new IllegalArgumentException();
        }
        if (stepIndex < 0 || stepIndex >= stepsTotal) {
            throw new IllegalArgumentException();
        }
        if (lastStepFrameId < 0) {
            throw new IllegalArgumentException();
        }
    }

    public ComboState advance(long frameId) {
        if (frameId < 0) {
            throw new IllegalArgumentException();
        }
        if (frameId < lastStepFrameId) {
            throw new IllegalArgumentException();
        }
        int nextIndex = Math.addExact(stepIndex, 1);
        if (nextIndex >= stepsTotal) {
            throw new IllegalArgumentException();
        }
        return new ComboState(actionId, weaponId, nextIndex, stepsTotal, frameId);
    }
}