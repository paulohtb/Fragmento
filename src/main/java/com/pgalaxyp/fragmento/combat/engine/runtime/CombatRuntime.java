package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.network.CombatSnapshotFingerprint;
import com.pgalaxyp.fragmento.combat.engine.network.CombatSnapshotSender;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatEffect;
import com.pgalaxyp.fragmento.combat.engine.profile.SpawnCutEffect;
import com.pgalaxyp.fragmento.combat.engine.restriction.CombatInventoryLock;
import com.pgalaxyp.fragmento.combat.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CombatRuntime {

    private final Map<UUID, CombatSnapshotFingerprint> lastFingerprint = new HashMap<>();

    private final CombatSnapshotSender snapshotSender = new CombatSnapshotSender();
    private final CombatInventoryLock inventoryLock = new CombatInventoryLock();

    private final CombatSessionManager sessions;

    public CombatRuntime() {
        this.sessions = CombatWiring.createSessionManager();
    }

    public CombatSessionManager sessions() {
        return sessions;
    }

    public void onAttackIntent(ServerPlayer player, AttackIntent intent) {
        if (player == null || intent == null) return;

        CombatSession session = sessions.sessionFor(player);
        CombatSession.Update up = session.onAttackIntent(player, intent);

        applyUpdate(player, up);
    }

    public void onAbilityIntent(ServerPlayer player, AbilityIntent intent) {
        if (player == null || intent == null) return;

        CombatSession session = sessions.sessionFor(player);
        CombatSession.Update up = session.onAbilityIntent(player, intent);

        applyUpdate(player, up);
    }

    public void onTick(ServerPlayer player) {
        if (player == null) return;

        CombatSession session = sessions.sessionFor(player);
        CombatSession.Update up = session.tick(player);

        applyUpdate(player, up);
    }

    public void onLogout(ServerPlayer player) {
        if (player == null) return;

        UUID id = player.getUUID();

        sessions.clear(player);
        lastFingerprint.remove(id);
        inventoryLock.clear(player);
    }

    private void applyUpdate(ServerPlayer player, CombatSession.Update up) {
        if (player == null || up == null) return;

        ServerCombatState state = up.state();
        List<CombatEffect> effects = up.effects();

        applyEffects(player, effects);
        syncInventoryLock(player, state);
        tickInventoryLock(player, state);
        sendIfChanged(player, state);
    }

    private void applyEffects(ServerPlayer player, List<CombatEffect> effects) {
        if (player == null || effects == null || effects.isEmpty()) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        for (CombatEffect e : effects) {
            if (e instanceof SpawnCutEffect s) {
                FragmentoEffectApplier.applySpawnCut(sl, s);
            }
        }
    }

    private void sendIfChanged(ServerPlayer player, ServerCombatState state) {
        if (player == null || state == null) return;

        var snap = state.snapshot();
        CombatSnapshotFingerprint now = CombatSnapshotFingerprint.of(snap);

        CombatSnapshotFingerprint last =
                lastFingerprint.get(player.getUUID());

        if (now.equals(last)) {
            return;
        }

        lastFingerprint.put(player.getUUID(), now);
        snapshotSender.send(player, snap);
    }

    private void syncInventoryLock(ServerPlayer player, ServerCombatState state) {
        if (player == null || state == null) return;

        if (state.loadout() == null || !state.loadout().valid()) {
            inventoryLock.sync(player, false);
            return;
        }

        CombatTime now = CombatTime.ofTicks(player.level().getGameTime());
        ActionLockState lock = state.lock();

        boolean shouldLock = lock != null && lock.itemSwapLocked(now);
        inventoryLock.sync(player, shouldLock);
    }

    private void tickInventoryLock(ServerPlayer player, ServerCombatState state) {
        if (player == null || state == null) return;

        if (state.loadout() == null || !state.loadout().valid()) {
            return;
        }

        CombatTime now = CombatTime.ofTicks(player.level().getGameTime());
        ActionLockState lock = state.lock();

        if (lock != null && lock.itemSwapLocked(now)) {
            inventoryLock.tick(player);
        }
    }
}