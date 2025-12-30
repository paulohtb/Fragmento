package com.pgalaxyp.fragmento.tier.command;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = "fragmento")
public final class TierCommandsBootstrap {

    private TierCommandsBootstrap() {}

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        RedeemCommand.register(event.getDispatcher());
    }
}