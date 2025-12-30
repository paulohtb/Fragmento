package com.pgalaxyp.fragmento.combat.state.session;

import com.pgalaxyp.fragmento.combat.state.action.ActiveActionState;
import com.pgalaxyp.fragmento.combat.state.combo.ComboRuntimeState;
import com.pgalaxyp.fragmento.combat.state.infusion.InfusionState;

public final class WeaponSessionState {

    private final ActiveActionState action = new ActiveActionState();
    private final ComboRuntimeState combo = new ComboRuntimeState();
    private final InfusionState infusion = new InfusionState();

    public ActiveActionState action() {
        return action;
    }

    public ComboRuntimeState combo() {
        return combo;
    }

    public InfusionState infusion() {
        return infusion;
    }

    public void clear() {
        action.clear();
        combo.reset();
        infusion.clear();
    }
}