package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.engine.restriction.CombatInteractionGuard;
import net.neoforged.neoforge.common.NeoForge;

public final class FragmentoCombatRuntime {

    private static volatile CombatRuntime RUNTIME;

    public static void init() {
        if (RUNTIME != null) {
            return;
        }

        synchronized (FragmentoCombatRuntime.class) {
            if (RUNTIME != null) {
                return;
            }

            CombatRuntime created = new CombatRuntime();

            NeoForge.EVENT_BUS.register(new CombatEventRouter(created));
            NeoForge.EVENT_BUS.register(new CombatInteractionGuard(created.sessions()));

            RUNTIME = created;

            FragmentoLog.runtime("combat runtime init ok");
        }
    }

    public static CombatRuntime get() {
        CombatRuntime rt = RUNTIME;
        if (rt == null) {
            init();
            rt = RUNTIME;
        }
        return rt;
    }

    private FragmentoCombatRuntime() {}
}