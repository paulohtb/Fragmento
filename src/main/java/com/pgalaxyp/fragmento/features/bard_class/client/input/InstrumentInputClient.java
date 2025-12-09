package com.pgalaxyp.fragmento.features.bard_class.client.input;

import com.pgalaxyp.fragmento.core.util.RaycastUtil;
import com.pgalaxyp.fragmento.features.bard_class.ability.NormalAbility;
import com.pgalaxyp.fragmento.features.bard_class.network.packet.AbilityPacket;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class InstrumentInputClient {

    private InstrumentInputClient() {}

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.screen != null) return;

        if (!InstrumentKeybinds.INSTRUMENT_USE.consumeClick()) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof InstrumentBase instrument)) return;

        int abilityIndex = 0;

        NormalAbility<?> ability = instrument.getAbility(abilityIndex);
        if (ability == null) return;

        double range = ability.getRange(stack);
        RaycastUtil.Result rc = RaycastUtil.perform(player, range);
        if (!rc.hasTarget()) return;

        PacketDistributor.sendToServer(new AbilityPacket(abilityIndex, rc.target().getId()));
    }
}
