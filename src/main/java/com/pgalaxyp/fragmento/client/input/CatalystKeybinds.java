package com.pgalaxyp.fragmento.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

@EventBusSubscriber(
        modid = "fragmento",
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public final class CatalystKeybinds {

    public static KeyMapping NORMAL_USE;
    public static KeyMapping SPECIAL_USE;

    private CatalystKeybinds() {
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        NORMAL_USE = new KeyMapping(
                "key.fragmento.normal_use",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.MOUSE,
                0,
                "key.categories.fragmento"
        );

        SPECIAL_USE = new KeyMapping(
                "key.fragmento.special_use",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.MOUSE,
                1,
                "key.categories.fragmento"
        );

        event.register(NORMAL_USE);
        event.register(SPECIAL_USE);
    }
}
