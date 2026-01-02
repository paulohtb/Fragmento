package com.pgalaxyp.fragmento.combat.state.runtime;

public final class ComboRuntimeState {

    private int comboIndex;

    public ComboRuntimeState() {
        this.comboIndex = 0;
    }

    public int comboIndex() {
        return comboIndex;
    }

    public void setComboIndex(int index) {
        this.comboIndex = Math.max(0, index);
    }

    public void resetCombo() {
        this.comboIndex = 0;
    }
}