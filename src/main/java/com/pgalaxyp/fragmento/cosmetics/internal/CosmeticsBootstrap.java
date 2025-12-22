package com.pgalaxyp.fragmento.cosmetics.internal;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import java.util.Objects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

@EventBusSubscriber(modid = CosmeticsKeys.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class CosmeticsBootstrap {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final CosmeticRegistryImpl REGISTRY;
    private static final CosmeticDataLoader LOADER;

    static {
        REGISTRY = new CosmeticRegistryImpl();
        CosmeticsRuntime.setRegistry(REGISTRY);
        LOADER = new CosmeticDataLoader(REGISTRY);
        LOGGER.info("CosmeticsBootstrap static init ok");
    }

    private CosmeticsBootstrap() {
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        Objects.requireNonNull(event, "event");
        event.addListener(LOADER);
        LOGGER.info("CosmeticsBootstrap onAddReloadListeners ok");
    }
}