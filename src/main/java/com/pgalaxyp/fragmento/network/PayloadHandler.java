package com.pgalaxyp.fragmento.network;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.NEW.NormalAbilityInputPacket;
import com.pgalaxyp.fragmento.NEW.SpecialAbilityInputPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Fragmento.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PayloadHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar payload = event.registrar(Fragmento.MODID).versioned("1.0");
        payload.playToServer(LeftClickPacket.TYPE, LeftClickPacket.CODEC, LeftClickPacket::handle);
        payload.playToServer(NormalAbilityInputPacket.TYPE, NormalAbilityInputPacket.CODEC, NormalAbilityInputPacket::handle);
        payload.playToServer(SpecialAbilityInputPacket.TYPE, SpecialAbilityInputPacket.CODEC, SpecialAbilityInputPacket::handle);
        payload.playToServer(ReplaceItemPacket.TYPE, ReplaceItemPacket.CODEC, ReplaceItemPacket::handle);
    }
}
