package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

public interface LevelAccessPolicy {
    boolean allowed(int playerLevel, int requiredLevel);
}