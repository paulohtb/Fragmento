package com.pgalaxyp.fragmento.combat.classModule.system;

import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameSystem;

public final class ClassAssignmentModule {
    public static FrameSystem createDefaultAssignmentSystem(ClassId defaultClassId) {
        return new DefaultClassAssignmentSystem(defaultClassId);
    }

    private ClassAssignmentModule() {}
}
