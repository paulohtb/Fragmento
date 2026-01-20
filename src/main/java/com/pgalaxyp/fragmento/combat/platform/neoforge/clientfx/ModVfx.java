package com.pgalaxyp.fragmento.combat.platform.neoforge.clientfx;

import com.mojang.blaze3d.vertex.*;
import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client.ClientModRuntime;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.GameSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

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
        BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffers.getBuffer(ModRenderTypes.HOMING_MAGIC);

        for (AbilitySnapshot a : snap.abilities().active()) {
            Entity src = findEntityByActorId(a.actorId());
            if (src == null) continue;

            double sx = src.getX(), sy = src.getY() + src.getEyeHeight(), sz = src.getZ();
            double ey = sy + 1.5;

            double rsx = sx - camPos.x, rsy = sy - camPos.y, rsz = sz - camPos.z;
            double rex = sx - camPos.x, rey = ey - camPos.y, rez = sz - camPos.z;

            int alpha = AbilityVisuals.fadeAlpha255(a, frameId, partialTick);

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
