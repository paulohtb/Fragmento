package com.pgalaxyp.fragmento.combat.actionModule.system;

import com.pgalaxyp.fragmento.combat.classModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;
import java.util.Map;

public final class ActionModule {
    public static FrameSystem createPrimaryActionSystem(Map<ClassId, ClassKit> classKits) {
        return new PrimaryActionSystem(classKits);
    }

    private ActionModule() {}
}
