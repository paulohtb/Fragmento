package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.combo.model.*;

public sealed interface ComboResult permits ComboResult.Progress, ComboResult.Reject {

    record Progress(
            ComboId comboId,
            int stepIndex,
            int stepsTotal,
            ComboStep step,
            boolean start,
            boolean end
    ) implements ComboResult {}

    enum Reject implements ComboResult { INSTANCE }

    static ComboResult reject() { return Reject.INSTANCE; }
}