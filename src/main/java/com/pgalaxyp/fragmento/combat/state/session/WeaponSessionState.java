package com.pgalaxyp.fragmento.combat.state.session;

import com.pgalaxyp.fragmento.combat.state.action.ActiveActionState;
import com.pgalaxyp.fragmento.combat.state.combo.ComboRuntimeState;
import com.pgalaxyp.fragmento.combat.state.infusion.InfusionState;

public final class WeaponSessionState {

    private final ActiveActionState action = new ActiveActionState();
    private final ComboRuntimeState combo = new ComboRuntimeState();
    private InfusionState infusion = InfusionState.empty();

    public ActiveActionState action() {
        return action;
    }

    public ComboRuntimeState combo() {
        return combo;
    }

    public InfusionState infusion() {
        return infusion;
    }

    public void setInfusion(InfusionState infusion) {
        this.infusion = infusion != null ? infusion : InfusionState.empty();
    }

    public void clear() {
        action.clear();
        combo.reset();
        infusion = InfusionState.empty();
    }
}