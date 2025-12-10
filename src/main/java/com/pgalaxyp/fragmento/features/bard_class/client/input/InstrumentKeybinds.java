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

    public static KeyMapping NORMAL_ABILITY_USE;
    public static KeyMapping SPECIAL_ABILITY_USE;

    private InstrumentKeybinds() {}

    @SubscribeEvent
    public static void onRegisterKeybindings(RegisterKeyMappingsEvent event) {

        NORMAL_ABILITY_USE = new KeyMapping(
                "key.fragmento.normal_ability_use",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.MOUSE,
                0,
                "key.categories.fragmento"
        );

        SPECIAL_ABILITY_USE = new KeyMapping(
                "key.fragmento.special_ability_use",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.MOUSE,
                1,
                "key.categories.fragmento"
        );

        event.register(NORMAL_ABILITY_USE);
        event.register(SPECIAL_ABILITY_USE);
    }
}
