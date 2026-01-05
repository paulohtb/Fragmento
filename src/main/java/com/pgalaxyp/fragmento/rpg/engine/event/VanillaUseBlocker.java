package com.pgalaxyp.fragmento.rpg.engine.event;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.bootstrap.RpgServices;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FragmentoMod.MODID)
public final class VanillaUseBlocker {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;

        if (RpgServices.runtime() == null) return;
        if (!RpgServices.runtime().catalystActive(player)) return;

        e.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;

        if (RpgServices.runtime() == null) return;
        if (!RpgServices.runtime().catalystActive(player)) return;

        e.setCanceled(true);
    }

    private VanillaUseBlocker() {}
}