package com.pgalaxyp.fragmento.combat.old.system.skill;

import com.pgalaxyp.fragmento.combat.old.system.charge.ChargeSystem;
import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.WeakHashMap;

public final class ServerSkillStateServices {

    private static final Map<MinecraftServer, ChargeSystem> CHARGES = new WeakHashMap<>();
    private static final Map<MinecraftServer, SkillStateSnapshotService> SNAPSHOTS = new WeakHashMap<>();

    private ServerSkillStateServices() {
    }

    public static ChargeSystem charges(MinecraftServer server) {
        if (server == null) throw new IllegalStateException("Server nulo");
        return CHARGES.computeIfAbsent(server, s -> new ChargeSystem());
    }

    public static SkillStateSnapshotService snapshots(MinecraftServer server) {
        if (server == null) throw new IllegalStateException("Server nulo");
        return SNAPSHOTS.computeIfAbsent(server, s -> new SkillStateSnapshotService(charges(s)));
    }

    public static void clear(MinecraftServer server) {
        if (server == null) return;
        CHARGES.remove(server);
        SNAPSHOTS.remove(server);
    }

    public static void clearAll() {
        CHARGES.clear();
        SNAPSHOTS.clear();
    }
}