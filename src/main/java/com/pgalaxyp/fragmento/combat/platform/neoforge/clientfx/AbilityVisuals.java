package com.pgalaxyp.fragmento.combat.platform.neoforge.clientfx;

import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;

public final class AbilityVisuals {

    public static float progress01(AbilitySnapshot a, long frameId, float partialTick) {
        float life = Math.max(1f, (float) (a.endFrameExclusive() - a.startFrame()));
        float age = (float) (frameId - a.startFrame()) + partialTick;
        float t = age / life;
        if (t <= 0f) return 0f;
        return Math.min(t, 1f);
    }

    public static int fadeAlpha255(AbilitySnapshot a, long frameId, float partialTick) {
        int v = Math.round((1f - progress01(a, frameId, partialTick)) * 255f);
        if (v < 0) return 0;
        return Math.min(v, 255);
    }

    private AbilityVisuals() {}
}
