package com.pgalaxyp.fragmento.rpg.platform.minecraft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.runtime.FragmentoClientRuntime;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.vfx.FragmentoVfx;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public final class FragmentoClientHooks {

    public static void onClientTick(ClientTickEvent.Post event) {
        FragmentoVfx.clientTick();
    }

    public static void onRenderLevel(RenderLevelStageEvent event) {
        PoseStack ps = event.getPoseStack();
        FragmentoVfx.render(ps, event.getPartialTick().getGameTimeDeltaPartialTick(true));
    }

    public static void onHud(RenderGuiOverlayEvent.Post event) {
        Optional<GameSnapshot> snapOpt = FragmentoClientRuntime.lastSnapshot();
        Optional<ActorId> idOpt = FragmentoClientRuntime.localActorId();
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
        font.draw(event.getPoseStack(), text, 8f, 8f, 0xFFFFFF);
    }

    private FragmentoClientHooks() {}
}