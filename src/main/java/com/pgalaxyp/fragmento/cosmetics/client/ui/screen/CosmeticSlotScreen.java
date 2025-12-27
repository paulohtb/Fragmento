package com.pgalaxyp.fragmento.cosmetics.client.ui.screen;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.client.lifecycle.CosmeticsClientNetwork;
import com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiEntry;
import com.pgalaxyp.fragmento.cosmetics.client.ui.CosmeticUiModel;
import com.pgalaxyp.fragmento.cosmetics.client.ui.action.CosmeticUiActions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CosmeticSlotScreen extends Screen {

    private static final int OUTER_MARGIN = 16;
    private static final int HEADER_Y = 10;
    private static final int TABS_Y = 28;
    private static final int LIST_TOP = 52;
    private static final int FOOTER_H = 34;
    private static final int ENTRY_H = 24;

    private final Screen parent;
    private final CosmeticSlot slot;

    private CosmeticList list;
    private long lastTierVersion;
    private long lastLoadoutVersion;

    public CosmeticSlotScreen(Screen parent, CosmeticSlot slot) {
        super(Component.literal("Cosmetics"));
        this.parent = parent;
        this.slot = slot == null ? CosmeticSlot.HEAD : slot;
    }

    @Override
    protected void init() {
        CosmeticsClientNetwork.requestSync();

        int contentWidth = computeContentWidth(this.width);
        int left = computeLeft(this.width, contentWidth);
        int right = Math.addExact(left, contentWidth);

        int bottom = Math.addExact(this.height, Math.negateExact(FOOTER_H));
        int listHeight = Math.addExact(bottom, Math.negateExact(LIST_TOP));

        this.list = new CosmeticList(this.minecraft, contentWidth, listHeight, LIST_TOP, ENTRY_H);
        this.list.setLeftPos(left);

        this.addWidget(this.list);
        this.addRenderableOnly(this.list);

        addTabs(left, contentWidth);
        addFooter(right);

        rebuild();
    }

    private static int computeContentWidth(int screenWidth) {
        int max = Math.addExact(screenWidth, Math.negateExact(Math.addExact(OUTER_MARGIN, OUTER_MARGIN)));
        if (max > 360) return 360;
        return Math.max(max, 220);
    }

    private static int computeLeft(int screenWidth, int contentWidth) {
        int space = Math.addExact(screenWidth, Math.negateExact(contentWidth));
        return space / 2;
    }

    private void addTabs(int left, int contentWidth) {
        CosmeticSlot[] values = CosmeticSlot.values();
        int count = values.length;

        int tabW = contentWidth / count;
        if (tabW < 40) tabW = 40;

        int x = left;
        for (int i = 0; i < count; i++) {
            CosmeticSlot s = values[i];
            int w = i == count - 1 ? Math.addExact(Math.addExact(left, contentWidth), Math.negateExact(x)) : tabW;

            Button tab = Button.builder(Component.literal(s.name()), new SwitchTabPress(this, parent, s))
                    .bounds(x, TABS_Y, w, 20)
                    .build();

            if (s == this.slot) {
                tab.active = false;
            }

            this.addRenderableWidget(tab);
            x = Math.addExact(x, w);
        }
    }

    private void addFooter(int right) {
        Button back = Button.builder(Component.literal("Back"), new BackPress(this))
                .bounds(Math.addExact(right, Math.negateExact(72)), Math.addExact(this.height, Math.negateExact(26)), 68, 20)
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

        if (this.list == null) return;

        List<CosmeticUiEntry> entries = CosmeticUiModel.build(this.slot);
        ArrayList<CosmeticEntry> built = new ArrayList<>(entries.size());

        for (CosmeticUiEntry e : entries) {
            if (e == null) continue;
            built.add(new CosmeticEntry(this, e));
        }

        this.list.setEntries(built);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gg, mouseX, mouseY, partialTick);
        super.render(gg, mouseX, mouseY, partialTick);
        gg.drawCenteredString(this.font, Component.literal("Cosmetics, " + this.slot.name()), this.width / 2, HEADER_Y, 16777215);
    }

    @Override
    public void onClose() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        mc.setScreen(parent);
    }

    private record BackPress(CosmeticSlotScreen screen) implements Button.OnPress {

        @Override
        public void onPress(Button button) {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;
            mc.setScreen(screen.parent);
        }
    }

    private record SwitchTabPress(CosmeticSlotScreen current, Screen parent, CosmeticSlot slot) implements Button.OnPress {

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

        public void setEntries(List<CosmeticEntry> entries) {
            this.replaceEntries(Objects.requireNonNullElseGet(entries, List::of));
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
        private final List<GuiEventListener> children;
        private final List<NarratableEntry> narratables;

        private CosmeticEntry(CosmeticSlotScreen screen, CosmeticUiEntry entry) {
            this.screen = screen;
            this.entry = entry;

            this.equip = Button.builder(Component.literal("Equip"), new EquipPress(entry))
                    .bounds(0, 0, 54, 18)
                    .build();
            this.unequip = Button.builder(Component.literal("Clear"), new ClearPress(entry))
                    .bounds(0, 0, 54, 18)
                    .build();

            this.children = List.of(this.equip, this.unequip);
            this.narratables = List.of(this.equip, this.unequip);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.narratables;
        }

        @Override
        public void render(GuiGraphics gg, int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTick) {
            CosmeticDefinition def = entry.definition();
            if (def == null) return;

            int textX = Math.addExact(x, 6);
            int textY = Math.addExact(y, 6);

            gg.drawString(screen.font, Component.literal(def.id().value()), textX, textY, 16777215);

            int btnX2 = Math.addExact(Math.addExact(x, rowWidth), Math.negateExact(60));
            int btnX1 = Math.addExact(btnX2, Math.negateExact(58));
            int btnY = Math.addExact(y, 3);

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
                int lockX = Math.addExact(btnX1, Math.negateExact(70));
                gg.drawString(screen.font, Component.literal("Locked"), lockX, textY, 16733525);
            } else if (equipped) {
                int onX = Math.addExact(btnX1, Math.negateExact(70));
                gg.drawString(screen.font, Component.literal("On"), onX, textY, 5635925);
            }
        }

        private record EquipPress(CosmeticUiEntry entry) implements Button.OnPress {

            @Override
            public void onPress(Button button) {
                CosmeticDefinition def = entry.definition();
                if (def == null) return;
                CosmeticUiActions.equip(def.slot(), def.id());
            }
        }

        private record ClearPress(CosmeticUiEntry entry) implements Button.OnPress {

            @Override
            public void onPress(Button button) {
                CosmeticDefinition def = entry.definition();
                if (def == null) return;
                CosmeticUiActions.unequip(def.slot());
            }
        }
    }
}