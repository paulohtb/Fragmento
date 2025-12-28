package com.pgalaxyp.fragmento.cosmetics.client.ui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CosmeticSlotScreen extends Screen {

    public CosmeticSlotScreen(Screen parent, com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot slot) {
        super(Component.literal("Cosmetics"));
        this.parent = parent;
        this.slot = slot == null ? com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot.HEAD : slot;
        this.tierLabel = Component.literal("");
    }

    private final Screen parent;
    private final com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot slot;

    private CosmeticList list;
    private long lastTierVersion;
    private long lastLoadoutVersion;
    private Component tierLabel;

    @Override
    protected void init() {
        com.pgalaxyp.fragmento.cosmetics.client.lifecycle.CosmeticsClientNetwork.requestSync();
        int contentWidth = computeContentWidth(this.width);
        int contentHeight = computeContentHeight(this.height);

        int left = (this.width - contentWidth) / 2;
        int top = (this.height - contentHeight) / 2;
        int right = left + contentWidth;
        int bottom = top + contentHeight;

        int listTop = top + HEADER_H + TABS_H + PANEL_PAD;
        int listBottom = bottom - FOOTER_H - PANEL_PAD;
        int listHeight = Math.max(40, listBottom - listTop);

        this.list = new CosmeticList(this.minecraft, contentWidth - (PANEL_PAD * 2), listHeight, listTop, ENTRY_H);
        this.list.setLeftPos(left + PANEL_PAD);

        this.addWidget(this.list);
        this.addRenderableOnly(this.list);

        addTabs(left + PANEL_PAD, top + HEADER_H, contentWidth - (PANEL_PAD * 2));
        addFooter(right - PANEL_PAD, bottom - PANEL_PAD);

        rebuild();
    }

    private static final int OUTER_MARGIN = 18;
    private static final int PANEL_PAD = 12;
    private static final int HEADER_H = 34;
    private static final int TABS_H = 22;
    private static final int FOOTER_H = 40;
    private static final int ENTRY_H = 26;

    private static int computeContentWidth(int screenWidth) {
        int max = screenWidth - (OUTER_MARGIN * 2);
        if (max > 520) max = 520;
        if (max < 300) max = 300;
        return max;
    }

    private static int computeContentHeight(int screenHeight) {
        int max = screenHeight - (OUTER_MARGIN * 2);
        if (max > 340) max = 340;
        if (max < 240) max = 240;
        return max;
    }

    private void addTabs(int left, int y, int width) {
        com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot[] values = com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot.values();
        int count = values.length;

        int tabW = width / count;
        if (tabW < 54) tabW = 54;

        int x = left;
        int end = left + width;

        for (int i = 0; i < count; i++) {
            com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot s = values[i];
            int w = (i == count - 1) ? (end - x) : tabW;

            Button tab = Button.builder(Component.literal(s.name()), new SwitchTabPress(this, parent, s))
                    .bounds(x, y, w, TABS_H)
                    .build();

            if (s == this.slot) {
                tab.active = false;
            }

            this.addRenderableWidget(tab);
            x += w;
            if (x >= end) break;
        }
    }

    private void addFooter(int right, int bottom) {
        Button back = Button.builder(Component.literal("Back"), new BackPress(this))
                .bounds(right - 72, bottom - 22, 72, 20)
                .build();
        this.addRenderableWidget(back);
    }

    @Override
    public void tick() {
        long tv = com.pgalaxyp.fragmento.tiers.client.TierClientState.version();
        long lv = currentLoadoutVersion();
        if (tv != this.lastTierVersion || lv != this.lastLoadoutVersion) {
            rebuild();
        }
    }

    private long currentLoadoutVersion() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) return 0L;
        return com.pgalaxyp.fragmento.cosmetics.client.CosmeticsClientState.getVersion(mc.player.getUUID());
    }

    private void rebuild() {
        this.lastTierVersion = com.pgalaxyp.fragmento.tiers.client.TierClientState.version();
        this.lastLoadoutVersion = currentLoadoutVersion();
        this.tierLabel = buildTierLabel(com.pgalaxyp.fragmento.tiers.client.TierClientState.get());

        if (this.list == null) return;

        java.util.List<com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiEntry> entries =
                com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiModel.build(this.slot);

        java.util.ArrayList<CosmeticEntry> built = new java.util.ArrayList<>(entries.size());
        for (com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiEntry e : entries) {
            if (e == null) continue;
            built.add(new CosmeticEntry(this, e));
        }
        this.list.setEntries(built);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gg, mouseX, mouseY, partialTick);

        int contentWidth = computeContentWidth(this.width);
        int contentHeight = computeContentHeight(this.height);
        int left = (this.width - contentWidth) / 2;
        int top = (this.height - contentHeight) / 2;
        int right = left + contentWidth;
        int bottom = top + contentHeight;

        gg.fill(left, top, right, bottom, 1879048192);
        gg.fill(left + 1, top + 1, right - 1, bottom - 1, 1442840576);

        int headerY = top + 10;
        gg.drawCenteredString(this.font, Component.literal("Cosmetics"), this.width / 2, headerY, 16777215);

        int tierX = left + PANEL_PAD;
        int tierY = top + 10;
        gg.drawString(this.font, this.tierLabel, tierX, tierY, 13421772);

        super.render(gg, mouseX, mouseY, partialTick);

        gg.fill(left + PANEL_PAD, top + HEADER_H - 2, right - PANEL_PAD, top + HEADER_H - 1, 1711276032);
        gg.fill(left + PANEL_PAD, top + HEADER_H + TABS_H, right - PANEL_PAD, top + HEADER_H + TABS_H + 1, 1711276032);
    }

    @Override
    public void onClose() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        mc.setScreen(parent);
    }

    private static Component buildTierLabel(com.pgalaxyp.fragmento.tiers.api.Tier t) {
        if (t == null) return Component.literal("Tier: ?");
        com.pgalaxyp.fragmento.tiers.api.TierStatus st = t.status();
        if (st == com.pgalaxyp.fragmento.tiers.api.TierStatus.ACTIVE) {
            return Component.literal("Tier: " + t.level().value());
        }
        if (st == com.pgalaxyp.fragmento.tiers.api.TierStatus.INACTIVE) {
            return Component.literal("Tier: 0");
        }
        if (st == com.pgalaxyp.fragmento.tiers.api.TierStatus.ERROR) {
            return Component.literal("Tier: error");
        }
        return Component.literal("Tier: ?");
    }

    private record BackPress(CosmeticSlotScreen screen) implements Button.OnPress {
        @Override
        public void onPress(Button button) {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            mc.setScreen(screen.parent);
        }
    }

    private record SwitchTabPress(CosmeticSlotScreen current, Screen parent, com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot slot)
            implements Button.OnPress {
        @Override
        public void onPress(Button button) {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            mc.setScreen(new CosmeticSlotScreen(parent, slot));
        }
    }

    private static final class CosmeticList extends ContainerObjectSelectionList<CosmeticEntry> {

        private int leftPos;

        private CosmeticList(Minecraft mc, int width, int height, int top, int itemHeight) {
            super(mc, width, height, top, itemHeight);
            this.leftPos = 0;
        }

        public void setLeftPos(int leftPos) {
            this.leftPos = leftPos;
        }

        public void setEntries(java.util.List<CosmeticEntry> entries) {
            this.replaceEntries(java.util.Objects.requireNonNullElseGet(entries, java.util.List::of));
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
        private final com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiEntry entry;
        private final Button equip;
        private final Button unequip;
        private final java.util.List<net.minecraft.client.gui.components.events.GuiEventListener> children;
        private final java.util.List<NarratableEntry> narratables;

        private CosmeticEntry(CosmeticSlotScreen screen, com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiEntry entry) {
            this.screen = screen;
            this.entry = entry;

            this.equip = Button.builder(Component.literal("Equip"), new EquipPress(entry))
                    .bounds(0, 0, 58, 18)
                    .build();
            this.unequip = Button.builder(Component.literal("Clear"), new ClearPress(entry))
                    .bounds(0, 0, 58, 18)
                    .build();

            this.children = java.util.List.of(this.equip, this.unequip);
            this.narratables = java.util.List.of(this.equip, this.unequip);
        }

        @Override
        public java.util.List<? extends net.minecraft.client.gui.components.events.GuiEventListener> children() {
            return this.children;
        }

        @Override
        public java.util.List<? extends NarratableEntry> narratables() {
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
            com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition def = entry.definition();
            if (def == null) return;

            int pad = 6;
            int textX = x + pad;
            int textY = y + 7;

            gg.fill(x, y + 1, x + rowWidth, y + rowHeight - 1, hovered ? 872415231 : 603979776);
            gg.drawString(screen.font, Component.literal(def.id().value()), textX, textY, 16777215);

            int btnX2 = x + rowWidth - 60;
            int btnX1 = btnX2 - 60;
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
                gg.drawString(screen.font, Component.literal("Locked"), btnX1 - 64, textY, 16733525);
            } else if (equipped) {
                gg.drawString(screen.font, Component.literal("On"), btnX1 - 64, textY, 5635925);
            }
        }

        private record EquipPress(com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiEntry entry) implements Button.OnPress {
            @Override
            public void onPress(Button button) {
                com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition def = entry.definition();
                if (def == null) return;
                com.pgalaxyp.fragmento.cosmetics.client.ui.action.CosmeticUiActions.equip(def.slot(), def.id());
            }
        }

        private record ClearPress(com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiEntry entry) implements Button.OnPress {
            @Override
            public void onPress(Button button) {
                com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition def = entry.definition();
                if (def == null) return;
                com.pgalaxyp.fragmento.cosmetics.client.ui.action.CosmeticUiActions.unequip(def.slot());
            }
        }
    }
}