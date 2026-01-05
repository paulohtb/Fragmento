package com.pgalaxyp.fragmento.rpg_old.runtime;

import com.pgalaxyp.fragmento.rpg_old.state.runtime.ActionLockState;
import net.minecraft.server.level.ServerPlayer;

public final class InventoryLockRuntime {

    public void sync(ServerPlayer player, ActionLockState lock) {
        if (player == null || lock == null) return;

        if (lock.itemSwapLockedUntil() != null) {
            player.getInventory().setChanged();
        }
    }
}