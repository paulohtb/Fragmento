package com.pgalaxyp.fragmento.rpg.runtime;

import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import com.pgalaxyp.fragmento.rpg.network.RpgSnapshotSender;
import com.pgalaxyp.fragmento.rpg.session.RpgSession;
import com.pgalaxyp.fragmento.rpg.session.RpgSessionManager;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.effect.runtime.EffectApplier;
import com.pgalaxyp.fragmento.rpg.effect.gameplay.SpawnCutEffect;
import com.pgalaxyp.fragmento.rpg.lock.InventoryLock;
import com.pgalaxyp.fragmento.rpg.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshotVersion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RpgRuntime {

    private final Map<UUID, CombatSnapshotVersion> lastSentVersion = new HashMap<>();

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

        RpgSession session = sessions.sessionFor(player);
        RpgSession.Update up = session.onAttackIntent(player, intent);

        applyUpdate(player, up);
    }

    public void onAbilityIntent(ServerPlayer player, AbilityIntent intent) {
        if (player == null || intent == null) return;

        RpgSession session = sessions.sessionFor(player);
        RpgSession.Update up = session.onAbilityIntent(player, intent);

        applyUpdate(player, up);
    }

    public void onTick(ServerPlayer player) {
        if (player == null) return;

        RpgSession session = sessions.sessionFor(player);
        RpgSession.Update up = session.tick(player);

        applyUpdate(player, up);
    }

    public void onLogout(ServerPlayer player) {
        if (player == null) return;

        UUID id = player.getUUID();

        sessions.clear(player);
        lastSentVersion.remove(id);
        inventoryLock.clear(player);
    }

    private void applyUpdate(ServerPlayer player, RpgSession.Update up) {
        if (player == null || up == null) return;

        ServerCombatState state = up.state();

        applyEffects(player, up.effects());
        syncInventoryLock(player, state);
        tickInventoryLock(player, state);
        sendIfVersionChanged(player, state);
    }

    private void applyEffects(ServerPlayer player, List<RpgEffect> effects) {
        if (effects == null || effects.isEmpty()) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        for (RpgEffect e : effects) {
            if (e instanceof SpawnCutEffect s) {
                EffectApplier.applySpawnCut(sl, s);
            }
        }
    }

    private void sendIfVersionChanged(ServerPlayer player, ServerCombatState state) {
        if (state == null || state.version() == null) return;

        UUID id = player.getUUID();
        CombatSnapshotVersion last = lastSentVersion.get(id);

        if (last != null && !state.version().isAfter(last)) {
            return;
        }

        lastSentVersion.put(id, state.version());
        snapshotSender.send(player, state.snapshot());
    }

    private void syncInventoryLock(ServerPlayer player, ServerCombatState state) {
        if (state == null || !state.loadout().valid()) {
            inventoryLock.sync(player, false);
            return;
        }

        Time now = Time.ofTicks(player.level().getGameTime());
        ActionLockState lock = state.lock();

        inventoryLock.sync(player, lock != null && lock.itemSwapLocked(now));
    }

    private void tickInventoryLock(ServerPlayer player, ServerCombatState state) {
        if (state == null || !state.loadout().valid()) return;

        Time now = Time.ofTicks(player.level().getGameTime());
        ActionLockState lock = state.lock();

        if (lock != null && lock.itemSwapLocked(now)) {
            inventoryLock.tick(player);
        }
    }
}