package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import java.util.*;

public interface ComboService {
    ComboResult decide(ComboId comboId, ComboPattern pattern, ComboInput input, Optional<ComboState> previous);
}
