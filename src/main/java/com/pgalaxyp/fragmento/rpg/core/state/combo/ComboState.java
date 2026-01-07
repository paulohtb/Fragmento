package com.pgalaxyp.fragmento.rpg.core.state.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ComboState(
        ActionId actionId,
        int index
) {
    public static ComboState empty() {
        return new ComboState(null, 0);
    }

    public boolean matches(ActionId id) {
        return actionId != null && actionId.equals(id);
    }

    public ComboState advance(ActionId id, int nextIndex) {
        return new ComboState(id, nextIndex);
    }
}