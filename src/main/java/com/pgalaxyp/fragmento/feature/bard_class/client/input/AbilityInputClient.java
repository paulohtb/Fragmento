package com.pgalaxyp.fragmento.feature.bard_class.client.input;

import com.pgalaxyp.fragmento.core.engine.RaycastBase;
import com.pgalaxyp.fragmento.feature.bard_class.common.data.InstrumentChargeData;
import com.pgalaxyp.fragmento.feature.bard_class.common.network.packet.BasicAbilityPacket;
import com.pgalaxyp.fragmento.feature.bard_class.common.weapon.InstrumentBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class AbilityInputClient {

    private AbilityInputClient() {}

    private static InstrumentBase getHeldInstrument(LocalPlayer player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            return null;
        }
        if (!(stack.getItem() instanceof InstrumentBase instrument)) {
            return null;
        }
        return instrument;
    }

    @SubscribeEvent
    public static void onMouse(InputEvent.MouseButton.Pre event) {
        if (event.getAction() != 1) {
            return;
        }

        if (event.getButton() != 0) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        if (mc.screen != null) {
            return;
        }

        InstrumentBase instrument = getHeldInstrument(player);
        if (instrument == null) {
            return;
        }

        if (player.getCooldowns().isOnCooldown(instrument)) {
            event.setCanceled(true);
            return;
        }

        ItemStack stack = player.getMainHandItem();

        int charge = InstrumentChargeData.getCharge(stack);
        int maxCharge = InstrumentChargeData.getMaxCharge();

        double range;
        if (charge >= maxCharge) {
            range = instrument.getChargedAbilityRange(stack);
        } else {
            range = instrument.getBasicAbilityRange(stack);
        }

        if (range <= 0.0D) {
            event.setCanceled(true);
            return;
        }

        RaycastBase.Result rayResult = RaycastBase.perform(player, range);
        if (!rayResult.hasEntityHit()) {
            event.setCanceled(true);
            return;
        }

        PacketDistributor.sendToServer(new BasicAbilityPacket());
        event.setCanceled(true);
    }
}
