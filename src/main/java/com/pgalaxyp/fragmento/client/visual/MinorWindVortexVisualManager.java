package com.pgalaxyp.fragmento.client.visual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public final class MinorWindVortexVisualManager {

    private static final List<MinorWindVortexVisual> EFFECTS = new ArrayList<>();

    private MinorWindVortexVisualManager() {
    }

    public static void clear() {
        EFFECTS.clear();
    }

    public static void spawn(Vec3 pos, int loopDuration, int gapDuration, int loops, float sizeXZ) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (pos == null) return;

        EFFECTS.add(new MinorWindVortexVisual(
                pos,
                mc.level.getGameTime(),
                loopDuration,
                gapDuration,
                loops,
                sizeXZ
        ));
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

        Iterator<MinorWindVortexVisual> it = EFFECTS.iterator();
        while (it.hasNext()) {
            MinorWindVortexVisual v = it.next();
            if (v.isExpired(partialTick)) {
                it.remove();
                continue;
            }
            v.render(poseStack, buffer, partialTick);
        }

        buffer.endBatch();
    }
}