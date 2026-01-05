package com.pgalaxyp.fragmento.rpg_old.engine.event;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.bootstrap.RpgServices;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = FragmentoMod.MODID)
public final class VanillaAttackBlocker {

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;

        if (RpgServices.runtime() == null) return;
        if (!RpgServices.runtime().catalystActive(player)) return;

        e.setCanceled(true);
    }

    private VanillaAttackBlocker() {}
}