package com.pgalaxyp.fragmento.rpg_old.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.pgalaxyp.fragmento.rpg_old.client.ClientContext;
import com.pgalaxyp.fragmento.rpg_old.client.ClientNetworkProxy;
import com.pgalaxyp.fragmento.rpg_old.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.AbilitySnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.CombatSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public final class ClientWorldRenderEvents {

    private static final SkillSlotId SPECIAL_SLOT = new SkillSlotId(2);

    public static void onRenderLevelStage(RenderLevelStageEvent e) {
        if (e == null) return;
        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.inGame(mc)) return;
        if (!ClientContext.catalystActive(mc)) return;
        if (mc.player == null) return;

        CombatSnapshot snap = ClientNetworkProxy.snapshot();
        if (snap == null) return;

        AbilitySnapshot ab = snap.abilities();
        if (ab == null) return;

        AbilitySnapshot.CastState cast = ab.casting().get(SPECIAL_SLOT);
        if (cast == null) return;

        Time now = ClientNetworkProxy.now();
        if (now == null) return;

        long remaining = Math.max(0L, cast.castEndsAt().ticks() - now.ticks());
        float progress = 1.0f - (remaining / 40.0f);
        if (progress < 0f) progress = 0f;
        if (progress > 1f) progress = 1f;

        double groundY = findGroundY(mc.player.level(), mc.player.position());
        if (Double.isNaN(groundY)) return;

        Vec3 cam = e.getCamera().getPosition();
        PoseStack ps = e.getPoseStack();

        ps.pushPose();
        ps.translate(-cam.x, -cam.y, -cam.z);

        Vec3 p = mc.player.position();
        ps.translate(p.x, groundY + 0.02, p.z);
        ps.mulPose(Axis.XP.rotationDegrees(90f));

        float size = 1.8f;
        float half = size * 0.5f;

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffers.getBuffer(RenderType.translucent());

        int a = (int) (80 + 175 * progress);
        float alpha = a / 255f;

        var pose = ps.last();

        vc.addVertex(pose.pose(), -half, -half, 0f)
                .setColor(1f, 1f, 1f, alpha)
                .setUv(0f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0x00F000F0)
                .setNormal(pose, 0f, 0f, 1f);

        vc.addVertex(pose.pose(), half, -half, 0f)
                .setColor(1f, 1f, 1f, alpha)
                .setUv(1f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0x00F000F0)
                .setNormal(pose, 0f, 0f, 1f);

        vc.addVertex(pose.pose(), half, half, 0f)
                .setColor(1f, 1f, 1f, alpha)
                .setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0x00F000F0)
                .setNormal(pose, 0f, 0f, 1f);

        vc.addVertex(pose.pose(), -half, half, 0f)
                .setColor(1f, 1f, 1f, alpha)
                .setUv(0f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0x00F000F0)
                .setNormal(pose, 0f, 0f, 1f);

        ps.popPose();
        buffers.endBatch(RenderType.translucent());
    }

    private static double findGroundY(Level level, Vec3 at) {
        if (level == null || at == null) return Double.NaN;

        BlockPos base = BlockPos.containing(at.x, at.y, at.z);
        int startY = Math.min(level.getMaxBuildHeight() - 1, Math.max(level.getMinBuildHeight(), base.getY() + 2));
        int endY = Math.max(level.getMinBuildHeight(), base.getY() - 10);

        for (int y = startY; y >= endY; y--) {
            BlockPos pos = new BlockPos(base.getX(), y, base.getZ());
            BlockState st = level.getBlockState(pos);
            if (!st.isAir()) {
                return y + 1.0;
            }
        }

        return Double.NaN;
    }

    private ClientWorldRenderEvents() {}
}