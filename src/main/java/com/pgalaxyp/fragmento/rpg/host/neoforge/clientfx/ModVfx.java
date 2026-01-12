package com.pgalaxyp.fragmento.rpg.host.neoforge.clientfx;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.events.event.HomingMagicVisualEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public final class ModVfx {

    private static final List<LineFx> active = new ArrayList<>();

    public static void accept(List<DomainEvent> events) {
        if (events == null) {
            throw new IllegalArgumentException();
        }
        for (DomainEvent e : events) {
            if (e instanceof HomingMagicVisualEvent(long frameId, int localIndex, QueryId queryId, ActorId sourceActorId, ActorId targetActorId, int lifetimeFrames)) {
                active.add(new LineFx(sourceActorId, targetActorId, lifetimeFrames));
            }
        }
    }

    public static void clientTick() {
        Iterator<LineFx> it = active.iterator();
        while (it.hasNext()) {
            LineFx fx = it.next();
            fx.age = Math.addExact(fx.age, 1);
            if (fx.age >= fx.lifetime) {
                it.remove();
            }
        }
    }

    public static void render(PoseStack poseStack, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffers.getBuffer(ModRenderTypes.HOMING_MAGIC);

        for (LineFx fx : active) {
            Entity src = findEntityByActorId(fx.source);
            Entity dst = findEntityByActorId(fx.target);
            if (src == null || dst == null) {
                continue;
            }

            double sx = src.getX();
            double sy = src.getY() + (double) src.getEyeHeight();
            double sz = src.getZ();

            double tx = dst.getX();
            double ty = dst.getY() + (double) dst.getEyeHeight();
            double tz = dst.getZ();

            float alpha = 1.0f;
            int r = 120;
            int g = 200;
            int b = 255;

            int a = (int) (alpha * 255f);
            var pose = poseStack.last();

            vc.addVertex(pose, (float) sx, (float) sy, (float) sz).setColor(r, g, b, a);
            vc.addVertex(pose, (float) tx, (float) ty, (float) tz).setColor(r, g, b, a);
        }

        buffers.endBatch(ModRenderTypes.HOMING_MAGIC);
    }

    private static Entity findEntityByActorId(ActorId id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return null;
        }
        for (Entity e : mc.level.entitiesForRendering()) {
            if (e == null) {
                continue;
            }
            if (new ActorId(e.getUUID()).equals(id)) {
                return e;
            }
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