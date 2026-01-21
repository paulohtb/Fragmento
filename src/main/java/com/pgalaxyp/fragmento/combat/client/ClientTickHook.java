package com.pgalaxyp.fragmento.combat.client;

public final class ClientTickHook {
    private final AbilityFeedbackRenderer abilityRenderer;
    private final DamageFeedbackRenderer damageRenderer;

    public ClientTickHook(AbilityFeedbackRenderer abilityRenderer, DamageFeedbackRenderer damageRenderer) {
        if (abilityRenderer == null || damageRenderer == null) throw new IllegalArgumentException();
        this.abilityRenderer = abilityRenderer;
        this.damageRenderer = damageRenderer;
    }

    public void onClientTick() {
        abilityRenderer.renderTick();
        damageRenderer.renderTick();
    }
}