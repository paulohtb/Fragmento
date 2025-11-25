package com.pgalaxyp.fragmento.client.bardClient;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pgalaxyp.fragmento.item.bard.weapon.AbstractWeapon;
import com.pgalaxyp.fragmento.network.ReplaceItemPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.Objects;

public class BardWeaponGUI extends Screen {
    private final Item[] items;
    private double rotation = 0;
    private int hover = -1;

    public BardWeaponGUI(Item[] inputItems, Item selected) {
        super(Component.empty());
        this.items = new Item[5];
        if (inputItems != null) {
            int n = Math.min(inputItems.length, 5);
            System.arraycopy(inputItems, 0, this.items, 0, n);
        }
    }

    private static Item firstNonNull(Item[] arr) {
        if (arr == null) return null;
        return Arrays.stream(arr).filter(Objects::nonNull).findFirst().orElse(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        double cx = width / 2.0, cy = height / 2.0;
        float totalRot = (float) (rotation - Math.PI / 2.0);
        hover = getSector(mx, my, cx, cy);

        VertexConsumer vc = g.bufferSource().getBuffer(RenderType.gui());
        Matrix4f m = g.pose().last().pose();

        for (int i = 0; i < 5; i++) {
            if (items[i] == null) continue;
            float start = (float) (i * 2 * Math.PI / 5.0 + totalRot) + (float) (Math.PI / 64.0);
            float end = (float) (i * 2 * Math.PI / 5.0 + totalRot + 2 * Math.PI / 5.0) - (float) (Math.PI / 64.0);
            int color = i == hover ? 0x8044AAFF : 0x80222222;
            drawRing(vc, m, cx, cy, 48, 96, start, end, color, 20);
        }
        drawRing(vc, m, cx, cy, 0, 32, 0, (float) (Math.PI * 2), 0x66222222, 20);

        double angle = Math.atan2(my - cy, mx - cx);
        drawThickLine(vc, m, cx, cy, cx + Math.cos(angle) * 96, cy + Math.sin(angle) * 96, 1.5, 0x99000000);
        drawRing(vc, m, cx + Math.cos(angle) * (96 - 8), cy + Math.sin(angle) * (96 - 8), 0, 4, 0, (float) (Math.PI * 2), 0xFFFFFFFF, 12);

        g.bufferSource().endBatch();

        for (int i = 0; i < 5; i++) {
            if (items[i] == null) continue;
            double mid = i * 2 * Math.PI / 5.0 + Math.PI / 5.0 + totalRot;
            double x = cx + Math.cos(mid) * (48 + 96) / 2.0;
            double y = cy + Math.sin(mid) * (48 + 96) / 2.0;
            renderItemScaled(g, items[i], x, y, i == hover ? 1.6f : 1.25f);
        }

        Item centerItem = hover >= 0 && items[hover] != null ? items[hover] : firstNonNull(items);
        if (centerItem != null) renderItemScaled(g, centerItem, cx, cy - 6, 1.9f);
    }

    private void unpackARGB(int argb, float[] out) {
        out[0] = ((argb >>> 16) & 0xFF) / 255f;
        out[1] = ((argb >>> 8) & 0xFF) / 255f;
        out[2] = (argb & 0xFF) / 255f;
        out[3] = ((argb >>> 24) & 0xFF) / 255f;
    }

    private void drawRing(VertexConsumer vc, Matrix4f m, double cx, double cy, double rInner, double rOuter, float start, float end, int argb, int segments) {
        if (end <= start) return;
        float[] c = new float[4];
        unpackARGB(argb, c);
        float step = (end - start) / segments;
        double cosStep = Math.cos(step), sinStep = Math.sin(step);
        double cosA = Math.cos(start), sinA = Math.sin(start);

        double ix1 = cx + cosA * rInner, iy1 = cy + sinA * rInner;
        double ox1 = cx + cosA * rOuter, oy1 = cy + sinA * rOuter;

        for (int i = 0; i < segments; i++) {
            double cosN = cosA * cosStep - sinA * sinStep;
            double sinN = sinA * cosStep + cosA * sinStep;

            double ix2 = cx + cosN * rInner, iy2 = cy + sinN * rInner;
            double ox2 = cx + cosN * rOuter, oy2 = cy + sinN * rOuter;

            vc.addVertex(m, (float) ix1, (float) iy1, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);
            vc.addVertex(m, (float) ix2, (float) iy2, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);
            vc.addVertex(m, (float) ox2, (float) oy2, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);
            vc.addVertex(m, (float) ox1, (float) oy1, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);

            ix1 = ix2; iy1 = iy2; ox1 = ox2; oy1 = oy2;
            cosA = cosN; sinA = sinN;
        }
    }

    private void drawThickLine(VertexConsumer vc, Matrix4f m, double x1, double y1, double x2, double y2, double thickness, int argb) {
        if (x1 == x2 && y1 == y2) return;
        float[] c = new float[4];
        unpackARGB(argb, c);
        double dx = x2 - x1, dy = y2 - y1, len = Math.sqrt(dx * dx + dy * dy);
        dx /= len; dy /= len;
        double offX = -dy * thickness * 0.5, offY = dx * thickness * 0.5;

        float x1a = (float) (x1 + offX), y1a = (float) (y1 + offY);
        float x1b = (float) (x1 - offX), y1b = (float) (y1 - offY);
        float x2a = (float) (x2 + offX), y2a = (float) (y2 + offY);
        float x2b = (float) (x2 - offX), y2b = (float) (y2 - offY);

        vc.addVertex(m, x1a, y1a, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);
        vc.addVertex(m, x1b, y1b, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);
        vc.addVertex(m, x2b, y2b, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);
        vc.addVertex(m, x2a, y2a, 0).setColor(c[0], c[1], c[2], c[3]).setUv(0,0).setOverlay(0).setLight(0x00F000F0).setNormal(0,0,1);
    }

    private void renderItemScaled(GuiGraphics g, Item item, double x, double y, float scale) {
        if (item == null) return;
        g.pose().pushPose();
        g.pose().translate(x - 8 * scale, y - 8 * scale, 200);
        g.pose().scale(scale, scale, 1);
        g.renderItem(new ItemStack(item), 0, 0);
        g.pose().popPose();
    }

    private int getSector(int mx, int my, double cx, double cy) {
        double dist = Math.hypot(mx - cx, my - cy);
        if (dist < 48 || dist > 96) return -1;
        double angle = Math.atan2(my - cy, mx - cx) - (rotation - Math.PI / 2.0);
        angle = (angle + 2 * Math.PI) % (2 * Math.PI);
        return (int) (angle / (2 * Math.PI / 5.0));
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (hover >= 0 && items[hover] != null) sendPacket(items[hover]);
        Minecraft.getInstance().setScreen(null);
        return true;
    }

    public boolean mouseScrolled(double x, double y, double delta) {
        rotation += delta > 0 ? Math.PI / 16 : -Math.PI / 16;
        return true;
    }

    private void sendPacket(Item item) {
        if (item == null) return;
        var conn = Minecraft.getInstance().getConnection();
        if (conn != null) conn.send(new ReplaceItemPacket(item, InteractionHand.MAIN_HAND));
    }

    public static boolean canOpen() {
        var mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getMainHandItem().getItem() instanceof AbstractWeapon;
    }
}