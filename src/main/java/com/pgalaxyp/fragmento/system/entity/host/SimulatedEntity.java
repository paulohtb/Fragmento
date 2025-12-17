package com.pgalaxyp.fragmento.system.entity.host;

import java.util.UUID;

public final class SimulatedEntity {

    private final int id;
    private final UUID ownerId;

    private double x;
    private double y;
    private double z;

    private boolean alive = true;

    public SimulatedEntity(int id, UUID ownerId, double x, double y, double z) {
        this.id = id;
        this.ownerId = ownerId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int id() {
        return id;
    }

    public UUID ownerId() {
        return ownerId;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public void move(double dx, double dy, double dz) {
        x += dx;
        y += dy;
        z += dz;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }
}