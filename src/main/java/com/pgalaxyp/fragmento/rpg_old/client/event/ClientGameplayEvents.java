package com.pgalaxyp.fragmento.rpg_old.client.event;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.rpg_old.client.ClientContext;
import com.pgalaxyp.fragmento.rpg_old.client.ClientNetworkProxy;
import com.pgalaxyp.fragmento.rpg_old.client.input.ClientInputController;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.LockSnapshot;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(
        modid = FragmentoMod.MODID,
        value = Dist.CLIENT
)
public final class ClientGameplayEvents {

    private static final ClientInputController INPUT = new ClientInputController();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post e) {
        INPUT.clientTick(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.catalystActive(mc)) return;

        if (shouldBlockHotbarSwap()) {
            e.setCanceled(true);
        }
    }

    private static boolean shouldBlockHotbarSwap() {
        CombatSnapshot snap = ClientNetworkProxy.snapshot();
        if (snap == null) return false;

        LockSnapshot lock = snap.lock();
        if (lock == null || lock.itemSwapLockedUntil() == null) return false;

        Time now = ClientNetworkProxy.now();
        Time until = lock.itemSwapLockedUntil();

        return now != null && now.ticks() < until.ticks();
    }

    private ClientGameplayEvents() {}
}