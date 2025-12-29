package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

public final class LevelAccessPolicies {

    public static final LevelAccessPolicy DEFAULT = new DefaultPolicy();

    private LevelAccessPolicies() {}

    private static final class DefaultPolicy implements LevelAccessPolicy {

        @Override
        public boolean allowed(int playerLevel, int requiredLevel) {
            if (requiredLevel <= 0) {
                return true;
            }
            if (playerLevel <= 0) {
                return false;
            }
            return playerLevel >= requiredLevel;
        }
    }
}