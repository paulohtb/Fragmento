package com.pgalaxyp.fragmento.combat.engine.restriction;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CombatInventoryLock {

    private record Snapshot(int selected, ItemStack main, ItemStack off) {
        private Snapshot(int selected, ItemStack main, ItemStack off) {
            this.selected = selected;
            this.main = main == null ? ItemStack.EMPTY : main.copy();
            this.off = off == null ? ItemStack.EMPTY : off.copy();
        }
    }

    private final Map<UUID, Snapshot> remembered = new HashMap<>();
    private final Map<UUID, Boolean> reentryGuard = new HashMap<>();

    public void sync(ServerPlayer player, boolean shouldLock) {
        if (player == null) return;

        UUID id = player.getUUID();
        boolean locked = remembered.containsKey(id);

        if (shouldLock && !locked) {
            remember(player);
            FragmentoLog.inventory("invLock enter, player.uuid={}", id);
            return;
        }

        if (!shouldLock && locked) {
            clear(player);
            FragmentoLog.inventory("invLock exit, player.uuid={}", id);
        }
    }

    public void tick(ServerPlayer player) {
        if (player == null) return;

        UUID id = player.getUUID();
        Snapshot snap = remembered.get(id);
        if (snap == null) return;

        if (Boolean.TRUE.equals(reentryGuard.get(id))) {
            return;
        }

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
            reentryGuard.put(id, Boolean.TRUE);
            try {
                player.inventoryMenu.broadcastChanges();
            } catch (Throwable t) {
                FragmentoLog.inventoryEx(t, "invLock broadcastChanges failed, player.uuid={}", id);
            } finally {
                reentryGuard.put(id, Boolean.FALSE);
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
        reentryGuard.put(id, Boolean.FALSE);

        FragmentoLog.inventory(
                "invLock remember, player.uuid={} selected={} main={} off={}",
                id,
                selected,
                stackKey(main),
                stackKey(off)
        );
    }

    public void clear(ServerPlayer player) {
        if (player == null) return;

        UUID id = player.getUUID();
        remembered.remove(id);
        reentryGuard.remove(id);
    }

    private static String stackKey(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "empty";
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key != null ? key + " x" + stack.getCount() : stack.getItem().toString() + " x" + stack.getCount();
    }
}