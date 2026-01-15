package com.pgalaxyp.fragmento.combat.action.api;

import com.pgalaxyp.fragmento.combat.action.model.*;

public sealed interface ActionRequest permits ActionRequest.Start, ActionRequest.Tick, ActionRequest.Cancel {

    record Start(ActionId actionId) implements ActionRequest {
        public Start { if (actionId == null) throw new IllegalArgumentException(); }
    }

    enum Tick implements ActionRequest { INSTANCE }
    enum Cancel implements ActionRequest { INSTANCE }
}