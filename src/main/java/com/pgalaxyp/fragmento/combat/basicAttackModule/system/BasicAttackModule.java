package com.pgalaxyp.fragmento.combat.basicAttackModule.system;

import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackDefinition;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Map;

public final class BasicAttackModule {
    public static BasicAttackSystems create(Map<WeaponId, BasicAttackDefinition> defs) {
        var engine = new BasicAttackEngine(defs);
        return new BasicAttackSystems(new BasicAttackCommandSystem(engine), new BasicAttackRuntimeSystem(engine));
    }

    private BasicAttackModule() {}
}
