package com.pgalaxyp.fragmento.feature.bard.common.combat;

import com.pgalaxyp.fragmento.feature.bard.common.config.FluteConstants;

public final class FluteSpiritCombatProfile implements BardWeaponProfile {

    public static final FluteSpiritCombatProfile INSTANCE = new FluteSpiritCombatProfile();

    private FluteSpiritCombatProfile() {
    }

    @Override
    public float getDamage(boolean charged) {
        return charged ? FluteConstants.CHARGED_DAMAGE : FluteConstants.BASIC_DAMAGE;
    }

    @Override
    public int getCooldown(boolean charged) {
        return charged ? FluteConstants.CHARGED_COOLDOWN : FluteConstants.BASIC_COOLDOWN;
    }

    @Override
    public double getRange(boolean charged) {
        return charged ? FluteConstants.CHARGED_RANGE : FluteConstants.BASIC_RANGE;
    }

    @Override
    public int getIdleTicks(boolean charged) {
        return charged ? FluteConstants.CHARGED_IDLE_TICKS : FluteConstants.BASIC_IDLE_TICKS;
    }

    @Override
    public int getTravelTicks(boolean charged) {
        return charged ? FluteConstants.CHARGED_TRAVEL_TICKS : FluteConstants.BASIC_TRAVEL_TICKS;
    }

    @Override
    public double getCollisionRadius(boolean charged) {
        return FluteConstants.FLUTE_COLLISION_RADIUS;
    }
}
