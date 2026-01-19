package com.pgalaxyp.fragmento.combat.platform.neoforge.clientfx;

import com.mojang.blaze3d.vertex.*;
import com.pgalaxyp.fragmento.combat.ability.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client.*;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;

public final class ModVfx {

    public static void clientTick() {}

    public static void render(PoseStack poseStack, float partialTick) {
        var snapOpt = ClientModRuntime.lastSnapshot();
        if (snapOpt.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        GameSnapshot snap = snapOpt.get();
        long frameId = snap.frame().frameId();

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffers.getBuffer(ModRenderTypes.HOMING_MAGIC);

        for (AbilityInstanceView a : snap.activeAbilities()) {
            Entity src = findEntityByActorId(a.actorId());
            if (src == null) continue;

            double sx = src.getX(), sy = src.getY() + src.getEyeHeight(), sz = src.getZ();
            double ex = sx, ey = sy + 1.5, ez = sz;

            double rsx = sx - camPos.x, rsy = sy - camPos.y, rsz = sz - camPos.z;
            double rex = ex - camPos.x, rey = ey - camPos.y, rez = ez - camPos.z;

            float life = Math.max(1f, (float) (a.endFrame() - a.startFrame()));
            float age = (float) (frameId - a.startFrame()) + partialTick;
            float t = Math.min(1f, Math.max(0f, age / life));
            int alpha = (int) ((1f - t) * 255f);

            var pose = poseStack.last();
            vc.addVertex(pose, (float) rsx, (float) rsy, (float) rsz).setColor(120, 200, 255, alpha);
            vc.addVertex(pose, (float) rex, (float) rey, (float) rez).setColor(120, 200, 255, alpha);
        }

        buffers.endBatch(ModRenderTypes.HOMING_MAGIC);
    }

    private static Entity findEntityByActorId(ActorId id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        for (Entity e : mc.level.entitiesForRendering()) if (e != null && new ActorId(e.getUUID()).equals(id)) return e;
        return null;
    }

    private ModVfx() {}
}
