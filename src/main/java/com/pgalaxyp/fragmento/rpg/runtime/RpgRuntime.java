package com.pgalaxyp.fragmento.rpg.runtime;

import com.pgalaxyp.fragmento.rpg.domain.execution.ExecutionFacts;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import com.pgalaxyp.fragmento.rpg.effect.gameplay.SpawnCutEffect;
import com.pgalaxyp.fragmento.rpg.effect.gameplay.SpawnInfusedStrikeEffect;
import com.pgalaxyp.fragmento.rpg.lock.InventoryLock;
import com.pgalaxyp.fragmento.rpg.network.RpgSnapshotSender;
import com.pgalaxyp.fragmento.rpg.session.RpgSession;
import com.pgalaxyp.fragmento.rpg.session.RpgSessionManager;
import com.pgalaxyp.fragmento.rpg.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshotVersion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class RpgRuntime {

    private final Map<UUID, CombatSnapshotVersion> lastSentVersion = new HashMap<>();
    private final Map<UUID, Set<UUID>> executionEntitiesByPlayer = new HashMap<>();

    private final RpgSnapshotSender snapshotSender = new RpgSnapshotSender();
    private final InventoryLock inventoryLock = new InventoryLock();

    private final RpgSessionManager sessions;

    public RpgRuntime() {
        this.sessions = RpgWiring.createSessionManager();
    }

    public RpgSessionManager sessions() {
        return sessions;
    }

    public void onAttackIntent(ServerPlayer player, AttackIntent intent) {
        if (player == null || intent == null) return;

        ExecutionFacts facts = executionFacts(player);

        RpgSession session = sessions.sessionFor(player);
        RpgSession.Update up = session.onAttackIntent(player, intent, facts);

        applyUpdate(player, up);
    }

    public void onAbilityIntent(ServerPlayer player, AbilityIntent intent) {
        if (player == null || intent == null) return;

        ExecutionFacts facts = executionFacts(player);

        RpgSession session = sessions.sessionFor(player);
        RpgSession.Update up = session.onAbilityIntent(player, intent, facts);

        applyUpdate(player, up);
    }

    public void onTick(ServerPlayer player) {
        if (player == null) return;

        ExecutionFacts facts = executionFacts(player);

        RpgSession session = sessions.sessionFor(player);
        RpgSession.Update up = session.tick(player, facts);

        applyUpdate(player, up);
    }

    public void onLogout(ServerPlayer player) {
        if (player == null) return;

        inventoryLock.clear(player);
        lastSentVersion.remove(player.getUUID());

        Set<UUID> ids = executionEntitiesByPlayer.remove(player.getUUID());
        if (ids != null && player.level() instanceof ServerLevel sl) {
            for (UUID id : ids) {
                if (id == null) continue;
                var e = sl.getEntity(id);
                if (e != null) e.discard();
            }
        }
    }

    private void applyUpdate(ServerPlayer player, RpgSession.Update up) {
        if (player == null || up == null) return;

        ServerCombatState state = up.state();
        if (state != null) {
            syncInventoryLock(player, state);
            syncExecutionEntities(player, state);
            sendSnapshotIfChanged(player, state);
        }

        List<RpgEffect> effects = up.effects();
        if (effects != null && !effects.isEmpty()) {
            applyEffects(player, effects);
        }
    }

    private void sendSnapshotIfChanged(ServerPlayer player, ServerCombatState state) {
        if (player == null || state == null) return;

        CombatSnapshotVersion next = state.version();
        if (next == null) return;

        UUID id = player.getUUID();
        CombatSnapshotVersion last = lastSentVersion.get(id);

        if (last != null && last.raw() >= next.raw()) {
            return;
        }

        lastSentVersion.put(id, next);
        snapshotSender.send(player, state.toSnapshot());
    }

    private void applyEffects(ServerPlayer player, List<RpgEffect> effects) {
        if (player == null || effects == null) return;

        for (RpgEffect e : effects) {
            switch (e) {
                case SpawnCutEffect cut -> EffectApplier.applyCut(player, cut);
                case SpawnInfusedStrikeEffect infused -> EffectApplier.applySpawnInfusedStrike(player, infused);
                case null, default -> {
                }
            }

        }
    }

    private void syncExecutionEntities(ServerPlayer player, ServerCombatState state) {
        if (player == null || state == null) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        ExecutionState exec = state.execution();
        if (exec == null) return;

        UUID pid = player.getUUID();
        Set<UUID> tracked = executionEntitiesByPlayer.computeIfAbsent(pid, k -> new HashSet<>());

        if (!exec.active()) {
            if (!tracked.isEmpty()) {
                for (UUID id : tracked) {
                    if (id == null) continue;
                    var ent = sl.getEntity(id);
                    if (ent != null) ent.discard();
                }
                tracked.clear();
            }
            return;
        }

        Set<UUID> live = new HashSet<>(tracked);
        if (!live.isEmpty()) {
            for (UUID id : live) {
                if (id == null) continue;
                var ent = sl.getEntity(id);
                if (ent == null || !ent.isAlive()) {
                    tracked.remove(id);
                }
            }
        }
    }

    private void syncInventoryLock(ServerPlayer player, ServerCombatState state) {
        if (player == null || state == null) return;

        boolean shouldLock = shouldLock(state, Time.ofTicks(player.level().getGameTime()));
        inventoryLock.sync(player, shouldLock);
        if (shouldLock) inventoryLock.tick(player);
    }

    private static boolean shouldLock(ServerCombatState state, Time now) {
        if (state == null || now == null) return false;

        ActionLockState lock = state.actionLock();
        if (lock == null || !lock.active()) return false;

        return lock.endsAt() == null || now.ticks() < lock.endsAt().ticks();
    }

    private static ExecutionFacts executionFacts(ServerPlayer player) {
        if (player == null) {
            return new ExecutionFacts(Time.ZERO, false, false);
        }

        long t = player.level().getGameTime();
        Time now = Time.ofTicks(t);

        boolean onGround = player.onGround();
        boolean inWater = player.isInWater();

        return new ExecutionFacts(now, onGround, inWater);
    }
}