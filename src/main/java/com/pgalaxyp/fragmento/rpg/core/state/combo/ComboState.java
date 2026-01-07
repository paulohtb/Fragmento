package com.pgalaxyp.fragmento.rpg.core.state.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ComboState(
        ActionId actionId,
        int index
) {
    public ComboState {
        index = Math.max(0, index);
    }

    public static ComboState empty() {
        return new ComboState(null, 0);
    }

    public boolean matches(ActionId id) {
        return actionId != null && actionId.equals(id);
    }
}