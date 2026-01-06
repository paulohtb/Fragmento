package com.pgalaxyp.fragmento.rpg.core.state.damage;

public final class HealthComponent {
    private double health;
    private final double max;

    public HealthComponent(double max) {
        if (max <= 0.0) throw new IllegalArgumentException("max <= 0");
        this.max = max;
        this.health = max;
    }

    public double health() {
        return health;
    }

    public double max() {
        return max;
    }

    public boolean alive() {
        return health > 0.0;
    }

    public void apply(double amount) {
        if (amount <= 0.0) return;
        health = Math.max(0.0, health - amount);
    }
}