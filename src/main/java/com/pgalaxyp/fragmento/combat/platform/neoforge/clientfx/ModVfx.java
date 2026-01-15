package com.pgalaxyp.fragmento.combat.platform.neoforge.clientfx;

import com.mojang.blaze3d.vertex.*;
import com.pgalaxyp.fragmento.combat.event.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.entity.*;
import net.minecraft.client.renderer.*;

public final class ModVfx {
    private static final List<LineFx> active = new ArrayList<>();

    public static void accept(List<DomainEvent> events) {
        if (events == null) throw new IllegalArgumentException();
        for (DomainEvent e : events) {
            if (e instanceof HomingMagicVisualEvent hm) active.add(new LineFx(hm.sourceActorId(), hm.targetActorId(), hm.lifetimeFrames()));
        }
    }

    public static void clientTick() {
        for (Iterator<LineFx> it = active.iterator(); it.hasNext();) {
            LineFx fx = it.next();
            fx.age = Math.addExact(fx.age, 1);
            if (fx.age >= fx.lifetime) it.remove();
        }
    }

    public static void render(PoseStack poseStack, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffers.getBuffer(ModRenderTypes.HOMING_MAGIC);
        for (LineFx fx : active) {
            Entity src = findEntityByActorId(fx.source);
            Entity dst = findEntityByActorId(fx.target);
            if (src == null || dst == null) continue;

            double sx = src.getX(), sy = src.getY() + (double) src.getEyeHeight(), sz = src.getZ();
            double tx = dst.getX(), ty = dst.getY() + (double) dst.getEyeHeight(), tz = dst.getZ();

            double rsx = sx - camPos.x, rsy = sy - camPos.y, rsz = sz - camPos.z;
            double rtx = tx - camPos.x, rty = ty - camPos.y, rtz = tz - camPos.z;

            float t = (fx.age + partialTick) / (float) fx.lifetime;
            if (t < 0f) t = 0f;
            if (t > 1f) t = 1f;
            float alpha = 1.0f - t;

            int r = 120, g = 200, b = 255, a = (int) (alpha * 255f);
            var pose = poseStack.last();

            vc.addVertex(pose, (float) rsx, (float) rsy, (float) rsz).setColor(r, g, b, a);
            vc.addVertex(pose, (float) rtx, (float) rty, (float) rtz).setColor(r, g, b, a);
        }

        buffers.endBatch(ModRenderTypes.HOMING_MAGIC);
    }

    private static Entity findEntityByActorId(ActorId id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;
        for (Entity e : mc.level.entitiesForRendering()) {
            if (e != null && new ActorId(e.getUUID()).equals(id)) return e;
        }
        return null;
    }

    private static final class LineFx {
        private final ActorId source;
        private final ActorId target;
        private final int lifetime;
        private int age;

        private LineFx(ActorId source, ActorId target, int lifetime) {
            this.source = source;
            this.target = target;
            this.lifetime = lifetime;
        }
    }

    private ModVfx() {}
}