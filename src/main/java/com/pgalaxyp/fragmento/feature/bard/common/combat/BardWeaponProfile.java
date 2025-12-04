package com.pgalaxyp.fragmento.feature.bard.common.combat;

public interface BardWeaponProfile {

    float getDamage(boolean charged);

    int getCooldown(boolean charged);

    double getRange(boolean charged);

    int getIdleTicks(boolean charged);

    int getTravelTicks(boolean charged);

    double getCollisionRadius(boolean charged);
}
