package com.pgalaxyp.fragmento.rpg.runtime;

import com.pgalaxyp.fragmento.rpg.domain.execution.ExecutionFacts;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.lock.InventoryLock;
import com.pgalaxyp.fragmento.rpg.network.RpgSnapshotSender;
import com.pgalaxyp.fragmento.rpg.session.RpgSession;
import com.pgalaxyp.fragmento.rpg.session.RpgSessionManager;
import com.pgalaxyp.fragmento.rpg.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RpgRuntime {

    private final RpgSessionManager sessions = RpgWiring.createSessionManager();
    private final RpgSnapshotSender snapshots = new RpgSnapshotSender();
    private final InventoryLock inventoryLock = new InventoryLock();

    private final Map<UUID, Integer> executionEntitiesByPlayer = new HashMap<>();

    public void onLogout(ServerPlayer player) {
        if (player == null) return;
        sessions.clear(player);
        executionEntitiesByPlayer.remove(player.getUUID());
        inventoryLock.clear(player);
    }

    public void onAttackIntent(ServerPlayer player, AttackIntent intent) {
        if (player == null || intent == null) return;

        Time now = now(player);
        RpgSession session = sessions.sessionFor(player);

        RpgSession.Update update = session.onAttackIntent(intent, executionFacts(player), now);
        applyUpdate(player, update.state(), now);
    }

    public void onAbilityIntent(ServerPlayer player, AbilityIntent intent) {
        if (player == null || intent == null) return;

        Time now = now(player);
        RpgSession session = sessions.sessionFor(player);

        RpgSession.Update update = session.onAbilityIntent(intent, executionFacts(player), now);
        applyUpdate(player, update.state(), now);
    }

    public void onTick(ServerPlayer player) {
        if (player == null) return;

        Time now = now(player);
        RpgSession session = sessions.sessionFor(player);

        RpgSession.Update update = session.tick(executionFacts(player), now);
        applyUpdate(player, update.state(), now);
    }

    public void onExecutionEntityTracked(ServerPlayer owner) {
        if (owner == null) return;
        UUID id = owner.getUUID();
        int next = executionEntitiesByPlayer.getOrDefault(id, 0) + 1;
        executionEntitiesByPlayer.put(id, next);
    }

    public void onExecutionEntityUntracked(ServerPlayer owner) {
        if (owner == null) return;
        UUID id = owner.getUUID();
        int cur = executionEntitiesByPlayer.getOrDefault(id, 0);
        int next = Math.max(0, cur - 1);
        if (next == 0) executionEntitiesByPlayer.remove(id);
        else executionEntitiesByPlayer.put(id, next);
    }

    public boolean catalystActive(ServerPlayer player) {
        if (player == null) return false;
        ServerCombatState state = sessions.sessionFor(player).state();
        return state != null && state.loadout() != null && state.loadout().valid();
    }

    public ServerCombatState state(ServerPlayer player) {
        if (player == null) return null;
        return sessions.sessionFor(player).state();
    }

    private ExecutionFacts executionFacts(ServerPlayer player) {
        if (player == null) return ExecutionFacts.empty();
        int trackedCount = executionEntitiesByPlayer.getOrDefault(player.getUUID(), 0);
        return new ExecutionFacts(trackedCount > 0);
    }

    private static Time now(ServerPlayer player) {
        if (player == null || player.level() == null) return Time.ofTicks(0L);
        return Time.ofTicks(player.level().getGameTime());
    }

    private void applyUpdate(ServerPlayer player, ServerCombatState updated, Time now) {
        if (player == null || updated == null) return;

        syncInventoryLock(player, updated, now);
        inventoryLock.tick(player);

        snapshots.send(player, updated.toSnapshot(now));
    }

    private void syncInventoryLock(ServerPlayer player, ServerCombatState state, Time now) {
        if (player == null || state == null) return;

        ActionLockState lock = state.lock();
        boolean shouldLock = lock != null && lock.isActive(now);

        inventoryLock.sync(player, shouldLock);
    }
}