package com.pgalaxyp.fragmento.rpg.host.minecraft.input;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.engine.input.Intent;
import com.pgalaxyp.fragmento.rpg.host.minecraft.items.RpgItemTags;
import com.pgalaxyp.fragmento.rpg.host.minecraft.lifecycle.ServerCombatLoop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class MinecraftInputAdapter {

    private final ServerCombatLoop combat;

    public MinecraftInputAdapter(ServerCombatLoop combat) {
        this.combat = combat;
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!isRpgWeapon(player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        combat.submit(Intent.primary(player.getId(), new ActionId("bard_flute_combo")), level);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!isRpgWeapon(player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        combat.submit(Intent.primary(player.getId(), new ActionId("bard_flute_combo")), level);
        event.setCanceled(true);
    }

    private boolean isRpgWeapon(ServerPlayer player) {
        var stack = player.getMainHandItem();
        return !stack.isEmpty() && stack.is(RpgItemTags.RPG_WEAPON);
    }
}