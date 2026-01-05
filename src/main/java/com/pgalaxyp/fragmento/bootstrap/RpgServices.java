package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.rpg.runtime.RpgRuntime;

public final class RpgServices {

    private static RpgRuntime runtime;

    public static void bind(RpgRuntime rt) {
        runtime = rt;
    }

    public static RpgRuntime runtime() {
        return runtime;
    }

    private RpgServices() {}
}