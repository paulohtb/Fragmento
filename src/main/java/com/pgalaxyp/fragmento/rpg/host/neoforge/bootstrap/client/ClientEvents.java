package com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pgalaxyp.fragmento.rpg.core.content.DefaultRpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.host.api.LocalActorProvider;
import com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.rpg.host.neoforge.clientfx.ModVfx;
import com.pgalaxyp.fragmento.rpg.host.neoforge.items.FragmentoItems;
import com.pgalaxyp.fragmento.rpg.input.minecraft.ItemWeaponBinding;
import com.pgalaxyp.fragmento.rpg.input.minecraft.NeoForgeInputBootstrap;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = FragmentoMod.MOD_ID, bus = Bus.GAME, value = Dist.CLIENT)
public final class ClientEvents {

    static {
        ClientRpgRuntime.install();

        ItemWeaponBinding mapping = new ItemWeaponBinding();
        mapping.register(FragmentoItems.FLUTE.get(), DefaultRpgContent.FLUTE);

        LocalActorProvider provider = ClientRpgRuntime::localActorId;

        NeoForgeInputBootstrap.ClientModule input = NeoForgeInputBootstrap.createClient(mapping, 2, provider);
        input.register(NeoForge.EVENT_BUS);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ModVfx.clientTick();
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        PoseStack ps = event.getPoseStack();
        ModVfx.render(ps, event.getPartialTick().getGameTimeDeltaPartialTick(true));
    }

    @SubscribeEvent
    public static void onHud(RenderGuiEvent.Post event) {
        Optional<GameSnapshot> snapOpt = ClientRpgRuntime.lastSnapshot();
        Optional<ActorId> idOpt = ClientRpgRuntime.localActorId();
        if (snapOpt.isEmpty() || idOpt.isEmpty()) {
            return;
        }

        var s = snapOpt.get().actors().get(idOpt.get());
        if (s == null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        String text = "RPG " + s.healthHearts() + "/" + s.maxHealthHearts();

        GuiGraphics gg = event.getGuiGraphics();
        gg.drawString(font, text, 8, 8, 0xFFFFFF, false);
    }

    private ClientEvents() {}
}