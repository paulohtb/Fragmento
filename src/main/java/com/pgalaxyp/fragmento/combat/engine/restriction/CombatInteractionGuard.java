package com.pgalaxyp.fragmento.combat.engine.restriction;

import com.pgalaxyp.fragmento.combat.engine.runtime.CombatSession;
import com.pgalaxyp.fragmento.combat.engine.runtime.CombatSessionManager;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public final class CombatInteractionGuard {

    private final CombatSessionManager sessions;

    public CombatInteractionGuard(CombatSessionManager sessions) {
        this.sessions = sessions;
    }

    private boolean shouldBlock(ServerPlayer player) {
        CombatSession session = sessions.sessionFor(player);
        ServerCombatState state = session.state();
        if (state == null) return false;

        if (state.loadout() == null || !state.loadout().valid()) return false;

        ActionLockState lock = state.lock();
        if (lock == null) return false;

        CombatTime now = CombatTime.ofTicks(player.level().getGameTime());
        return lock.blocksVanillaInteraction(now);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!shouldBlock(player)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!shouldBlock(player)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!shouldBlock(player)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!shouldBlock(player)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (!shouldBlock(player)) return;
        event.setCanceled(true);
    }
}