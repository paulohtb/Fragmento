package com.pgalaxyp.fragmento.cosmetics.client.ui.screen;

import com.pgalaxyp.fragmento.cosmetics.client.network.CosmeticsClientNetwork;
import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientState;
import com.pgalaxyp.fragmento.cosmetics.client.ui.action.CosmeticUiActions;
import com.pgalaxyp.fragmento.cosmetics.client.ui.model.CosmeticUiEntry;
import com.pgalaxyp.fragmento.cosmetics.client.ui.model.CosmeticUiModel;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientViews;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CosmeticSlotScreen extends Screen {

    private static final int OUTER_MARGIN = 18;
    private static final int PANEL_PAD = 12;
    private static final int HEADER_H = 34;
    private static final int TABS_H = 22;
    private static final int FOOTER_H = 40;
    private static final int ENTRY_H = 26;

    private final Screen parent;
    private final CosmeticSlot slot;

    private CosmeticList list;
    private long lastEntitlementVersion;
    private long lastLoadoutVersion;
    private Component entitlementLabel;

    public CosmeticSlotScreen(Screen parent, CosmeticSlot slot) {
        super(Component.literal("Cosmetics"));
        this.parent = parent;
        this.slot = slot == null ? CosmeticSlot.HEAD : slot;
        this.entitlementLabel = Component.literal("");
    }

    @Override
    protected void init() {
        super.init();

        CosmeticsClientNetwork.requestSync();

        int contentWidth = computeContentWidth(this.width);
        int contentHeight = computeContentHeight(this.height);

        int left = subInt(this.width, contentWidth) / 2;
        int top = subInt(this.height, contentHeight) / 2;
        int right = left + contentWidth;
        int bottom = top + contentHeight;

        int innerW = subInt(contentWidth, PANEL_PAD * 2);

        int listTop = top + HEADER_H + TABS_H + PANEL_PAD;
        int listBottom = subInt(bottom, FOOTER_H + PANEL_PAD);
        int listHeight = Math.max(40, subInt(listBottom, listTop));

        this.list = new CosmeticList(this.minecraft, innerW, listHeight, listTop, ENTRY_H, left + PANEL_PAD);
        this.addRenderableWidget(this.list);

        addTabs(left + PANEL_PAD, top + HEADER_H, innerW);
        addFooter(subInt(right, PANEL_PAD), subInt(bottom, PANEL_PAD));

        rebuild();
    }

    private static int computeContentWidth(int screenWidth) {
        int max = subInt(screenWidth, OUTER_MARGIN * 2);
        if (max > 520) {
            max = 520;
        }
        if (max < 300) {
            max = 300;
        }
        return max;
    }

    private static int computeContentHeight(int screenHeight) {
        int max = subInt(screenHeight, OUTER_MARGIN * 2);
        if (max > 340) {
            max = 340;
        }
        if (max < 240) {
            max = 240;
        }
        return max;
    }

    private void addTabs(int left, int y, int width) {
        CosmeticSlot[] values = CosmeticSlot.values();
        int count = values.length;
        if (count == 0) {
            return;
        }

        int base = width / count;
        int rem = width % count;

        int x = left;

        for (int i = 0; i < count; i++) {
            CosmeticSlot s = values[i];
            int w = base + (i < rem ? 1 : 0);

            Button tab = Button.builder(Component.literal(s.name()), new SwitchTabPress(this, parent, s))
                    .bounds(x, y, w, TABS_H)
                    .build();

            if (s == this.slot) {
                tab.active = false;
            }

            this.addRenderableWidget(tab);
            x += w;
        }
    }

    private void addFooter(int right, int bottom) {
        Button back = Button.builder(Component.literal("Back"), new BackPress(this))
                .bounds(subInt(right, 72), subInt(bottom, 22), 72, 20)
                .build();
        this.addRenderableWidget(back);
    }

    @Override
    public void tick() {
        CosmeticEntitlementClientView ev = CosmeticEntitlementClientViews.view();
        long entV = ev == null ? 0L : ev.version();
        long loadV = currentLoadoutVersion();
        if (entV != this.lastEntitlementVersion || loadV != this.lastLoadoutVersion) {
            rebuild();
        }
    }

    private long currentLoadoutVersion() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) {
            return 0L;
        }
        return CosmeticsClientState.getVersion(mc.player.getUUID());
    }

    private void rebuild() {
        CosmeticEntitlementClientView view = CosmeticEntitlementClientViews.view();
        this.lastEntitlementVersion = view == null ? 0L : view.version();
        this.lastLoadoutVersion = currentLoadoutVersion();

        String label = view == null ? "Tier: ?" : Objects.toString(view.label(), "Tier: ?");
        this.entitlementLabel = Component.literal(label);

        if (this.list == null) {
            return;
        }

        List<CosmeticUiEntry> entries = CosmeticUiModel.build(this.slot);

        ArrayList<CosmeticEntry> built = new ArrayList<>(entries.size());
        for (CosmeticUiEntry e : entries) {
            if (e == null) {
                continue;
            }
            built.add(new CosmeticEntry(this, e));
        }
        this.list.setEntries(built);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gg, mouseX, mouseY, partialTick);

        int contentWidth = computeContentWidth(this.width);
        int contentHeight = computeContentHeight(this.height);
        int left = subInt(this.width, contentWidth) / 2;
        int top = subInt(this.height, contentHeight) / 2;
        int right = left + contentWidth;
        int bottom = top + contentHeight;

        gg.fill(left, top, right, bottom, 1879048192);
        gg.fill(left + 1, top + 1, subInt(right, 1), subInt(bottom, 1), 1442840576);

        gg.drawCenteredString(this.font, Component.literal("Cosmetics"), this.width / 2, top + 10, 16777215);
        gg.drawString(this.font, this.entitlementLabel, left + PANEL_PAD, top + 10, 13421772);

        super.render(gg, mouseX, mouseY, partialTick);

        gg.fill(left + PANEL_PAD, subInt(top + HEADER_H, 2), subInt(right, PANEL_PAD), subInt(top + HEADER_H, 1), 1711276032);
        gg.fill(left + PANEL_PAD, top + HEADER_H + TABS_H, subInt(right, PANEL_PAD), top + HEADER_H + TABS_H + 1, 1711276032);
    }

    @Override
    public void onClose() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) {
            return;
        }
        mc.setScreen(parent);
    }

    private static int subInt(int a, int b) {
        return Math.addExact(a, Math.negateExact(b));
    }

    private record BackPress(CosmeticSlotScreen screen) implements Button.OnPress {
        @Override
        public void onPress(Button button) {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) {
                return;
            }
            mc.setScreen(screen.parent);
        }
    }

    private record SwitchTabPress(CosmeticSlotScreen current, Screen parent, CosmeticSlot slot) implements Button.OnPress {
        @Override
        public void onPress(Button button) {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) {
                return;
            }
            mc.setScreen(new CosmeticSlotScreen(parent, slot));
        }
    }

    private static final class CosmeticList extends ContainerObjectSelectionList<CosmeticEntry> {

        private final int leftPos;

        private CosmeticList(Minecraft mc, int width, int height, int top, int itemHeight, int leftPos) {
            super(mc, width, height, top, itemHeight);
            this.leftPos = leftPos;
        }

        public void setEntries(List<CosmeticEntry> entries) {
            this.replaceEntries(entries == null ? List.of() : entries);
        }

        @Override
        public int getRowLeft() {
            return this.leftPos;
        }

        @Override
        public int getRowWidth() {
            return this.width;
        }
    }

    private static final class CosmeticEntry extends ContainerObjectSelectionList.Entry<CosmeticEntry> {

        private final CosmeticSlotScreen screen;
        private final CosmeticUiEntry entry;
        private final Button equip;
        private final Button unequip;
        private final List<net.minecraft.client.gui.components.events.GuiEventListener> children;
        private final List<NarratableEntry> narratables;

        private CosmeticEntry(CosmeticSlotScreen screen, CosmeticUiEntry entry) {
            this.screen = screen;
            this.entry = entry;

            this.equip = Button.builder(Component.literal("Equip"), new EquipPress(entry))
                    .bounds(0, 0, 58, 18)
                    .build();
            this.unequip = Button.builder(Component.literal("Clear"), new ClearPress(entry))
                    .bounds(0, 0, 58, 18)
                    .build();

            this.children = List.of(this.equip, this.unequip);
            this.narratables = List.of(this.equip, this.unequip);
        }

        @Override
        public List<? extends net.minecraft.client.gui.components.events.GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.narratables;
        }

        @Override
        public void render(
                GuiGraphics gg,
                int index,
                int y,
                int x,
                int rowWidth,
                int rowHeight,
                int mouseX,
                int mouseY,
                boolean hovered,
                float partialTick
        ) {
            CosmeticDefinition def = entry.definition();
            if (def == null) {
                return;
            }

            int pad = 6;
            int textX = x + pad;
            int textY = y + 7;

            gg.fill(x, y + 1, x + rowWidth, subInt(y + rowHeight, 1), hovered ? 872415231 : 603979776);
            gg.drawString(screen.font, Component.literal(def.id().value()), textX, textY, 16777215);

            int btnX2 = subInt(x + rowWidth, 60);
            int btnX1 = subInt(btnX2, 60);
            int btnY = y + 4;

            this.equip.setX(btnX1);
            this.equip.setY(btnY);
            this.unequip.setX(btnX2);
            this.unequip.setY(btnY);

            boolean allowed = entry.allowed();
            boolean equipped = entry.equipped();

            this.equip.active = allowed && !equipped;
            this.unequip.active = equipped;

            this.equip.render(gg, mouseX, mouseY, partialTick);
            this.unequip.render(gg, mouseX, mouseY, partialTick);

            if (!allowed) {
                gg.drawString(screen.font, Component.literal("Locked"), subInt(btnX1, 64), textY, 16733525);
                return;
            }
            if (equipped) {
                gg.drawString(screen.font, Component.literal("On"), subInt(btnX1, 64), textY, 5635925);
            }
        }

        private record EquipPress(CosmeticUiEntry entry) implements Button.OnPress {
            @Override
            public void onPress(Button button) {
                CosmeticDefinition def = entry.definition();
                if (def == null) {
                    return;
                }
                CosmeticUiActions.equip(def.slot(), def.id());
            }
        }

        private record ClearPress(CosmeticUiEntry entry) implements Button.OnPress {
            @Override
            public void onPress(Button button) {
                CosmeticDefinition def = entry.definition();
                if (def == null) {
                    return;
                }
                CosmeticUiActions.unequip(def.slot());
            }
        }
    }
}