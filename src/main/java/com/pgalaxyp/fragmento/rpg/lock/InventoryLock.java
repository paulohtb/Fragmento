package com.pgalaxyp.fragmento.rpg.lock;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class InventoryLock {

    private record Snapshot(int selected, ItemStack main, ItemStack off) {
        private Snapshot(int selected, ItemStack main, ItemStack off) {
            this.selected = selected;
            this.main = main == null ? ItemStack.EMPTY : main.copy();
            this.off = off == null ? ItemStack.EMPTY : off.copy();
        }
    }

    private final Map<UUID, Snapshot> remembered = new HashMap<>();
    private final Set<UUID> reentryGuard = new HashSet<>();

    public void sync(ServerPlayer player, boolean shouldLock) {
        if (player == null) return;

        UUID id = player.getUUID();
        boolean locked = remembered.containsKey(id);

        if (shouldLock && !locked) {
            remember(player);
            return;
        }

        if (!shouldLock && locked) {
            clear(player);
        }
    }

    public void tick(ServerPlayer player) {
        if (player == null) return;

        UUID id = player.getUUID();
        Snapshot snap = remembered.get(id);
        if (snap == null) return;

        if (reentryGuard.contains(id)) return;

        int selected = player.getInventory().selected;
        ItemStack main = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack off = player.getItemBySlot(EquipmentSlot.OFFHAND);

        boolean changed = false;

        if (selected != snap.selected) {
            player.getInventory().selected = snap.selected;
            changed = true;
        }

        if (!ItemStack.isSameItemSameComponents(main, snap.main)) {
            player.setItemSlot(EquipmentSlot.MAINHAND, snap.main.copy());
            changed = true;
        }

        if (!ItemStack.isSameItemSameComponents(off, snap.off)) {
            player.setItemSlot(EquipmentSlot.OFFHAND, snap.off.copy());
            changed = true;
        }

        if (changed) {
            reentryGuard.add(id);
            try {
                player.inventoryMenu.broadcastChanges();
            } finally {
                reentryGuard.remove(id);
            }
        }
    }

    public void remember(ServerPlayer player) {
        if (player == null) return;

        UUID id = player.getUUID();
        int selected = player.getInventory().selected;
        ItemStack main = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack off = player.getItemBySlot(EquipmentSlot.OFFHAND);

        remembered.put(id, new Snapshot(selected, main, off));
        reentryGuard.remove(id);
    }

    public void clear(ServerPlayer player) {
        if (player == null) return;

        UUID id = player.getUUID();
        remembered.remove(id);
        reentryGuard.remove(id);
    }
}