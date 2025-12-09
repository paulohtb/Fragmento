package com.pgalaxyp.fragmento.features.bard_class.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class InstrumentKeybinds {

    public static KeyMapping INSTRUMENT_USE;

    private InstrumentKeybinds() {}

    @SubscribeEvent
    public static void onRegisterKeybindings(RegisterKeyMappingsEvent event) {
        INSTRUMENT_USE = new KeyMapping(
                "key.fragmento.instrument_use",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.MOUSE,
                0,
                "key.categories.fragmento"
        );

        event.register(INSTRUMENT_USE);
    }
}
