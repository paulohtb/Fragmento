package com.pgalaxyp.fragmento.client.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public final class AirImpactVisualManager {

    private static final List<AirImpactVisual> EFFECTS = new ArrayList<>();

    private AirImpactVisualManager() {
    }

    public static void clear() {
        EFFECTS.clear();
    }

    public static void spawn(Vec3 pos) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (pos == null) return;
        EFFECTS.add(new AirImpactVisual(pos, mc.level.getGameTime()));
    }

    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if (EFFECTS.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            EFFECTS.clear();
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        Iterator<AirImpactVisual> it = EFFECTS.iterator();
        while (it.hasNext()) {
            AirImpactVisual v = it.next();
            if (v.isExpired(partialTick)) {
                it.remove();
                continue;
            }
            v.render(poseStack, buffer, partialTick);
        }

        buffer.endBatch();
    }
}