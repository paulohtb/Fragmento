package com.pgalaxyp.fragmento.core.debug;

import net.minecraft.world.entity.Entity;

public final class ModLogger {

    private static boolean enabled = true;

    private ModLogger() {
    }

    public static void setEnabled(boolean v) {
        enabled = v;
    }

    private static void out(String category, String msg) {
        if (!enabled) return;
        System.out.println("[FRAGMENTO][" + category + "] " + msg);
    }

    public static void log(String msg) {
        out("GENERAL", msg);
    }

    public static void tickEntity(Entity e) {
        if (!enabled) return;
        out("ENTITY_TICK", "type=" + e.getType().toString() + " id=" + e.getId() + " pos=" + e.position());
    }

    public static void controllerTick(Entity e, String controller) {
        if (!enabled) return;
        out("CONTROLLER", "name=" + controller + " entityId=" + e.getId() + " pos=" + e.position());
    }

    public static void behaviorTick(Entity e, String behavior, int lifetime) {
        if (!enabled) return;
        out("BEHAVIOR", "name=" + behavior + " entityId=" + e.getId() + " life=" + lifetime);
    }

    public static void ability(String name, Entity caster, Entity target) {
        if (!enabled) return;
        int targetId = target == null ? -1 : target.getId();
        out("ABILITY", "name=" + name + " casterId=" + caster.getId() + " targetId=" + targetId);
    }

    public static void abilityResult(String name, boolean success, int cooldown) {
        out("ABILITY_RESULT", "name=" + name + " success=" + success + " cooldown=" + cooldown);
    }

    public static void spawn(Entity e) {
        if (!enabled) return;
        out("ENTITY_SPAWN", "type=" + e.getType().toString() + " id=" + e.getId() + " pos=" + e.position());
    }

    public static void discard(Entity e) {
        if (!enabled) return;
        out("ENTITY_DISCARD", "type=" + e.getType().toString() + " id=" + e.getId());
    }

    public static void state(Entity e, String state) {
        if (!enabled) return;
        out("STATE", "value=" + state + " entityId=" + e.getId());
    }

    public static void collision(Entity e, Entity target) {
        if (!enabled) return;
        out("COLLISION", "selfId=" + e.getId() + " targetId=" + target.getId());
    }

    public static void network(String channel, String msg) {
        out("NETWORK", "channel=" + channel + " " + msg);
    }

    public static void input(String source, String msg) {
        out("INPUT", "source=" + source + " " + msg);
    }
}
