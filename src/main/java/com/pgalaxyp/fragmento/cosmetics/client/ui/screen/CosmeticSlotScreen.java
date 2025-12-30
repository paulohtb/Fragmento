package com.pgalaxyp.fragmento.cosmetics.client.ui.screen;

import com.pgalaxyp.fragmento.cosmetics.client.state.ClientCosmetics;
import com.pgalaxyp.fragmento.cosmetics.client.ui.action.CosmeticUiActions;
import com.pgalaxyp.fragmento.cosmetics.client.ui.model.CosmeticUiEntry;
import com.pgalaxyp.fragmento.cosmetics.client.ui.model.CosmeticUiModel;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.tiers.client.state.TierClientState;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

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
    private long lastRosterVersion;
    private long lastTierVersion;
    private Component tierLabel;
    private List<CosmeticUiEntry> lastUiEntries;

    public CosmeticSlotScreen(Screen parent, CosmeticSlot slot) {
        super(Component.literal("Cosmetics"));
        this.parent = parent;
        this.slot = slot == null ? CosmeticSlot.HEAD : slot;
        this.tierLabel = Component.literal("");
        this.lastUiEntries = List.of();
        this.lastRosterVersion = 0L;
        this.lastTierVersion = 0L;
    }

    @Override
    protected void init() {
        super.init();

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

        refresh(true);
    }

    @Override
    public void tick() {
        refresh(false);
    }

    private void refresh(boolean force) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) return;

        UUID selfId = mc.player.getUUID();
        long rosterV = ClientCosmetics.rosterVersion(selfId);
        long tierV = TierClientState.version();

        if (!force && rosterV == this.lastRosterVersion && tierV == this.lastTierVersion) return;

        this.lastRosterVersion = rosterV;
        this.lastTierVersion = tierV;

        this.tierLabel = Component.literal("Tier: " + TierClientState.level());

        List<CosmeticUiEntry> uiEntries = CosmeticUiModel.build(selfId, this.slot);
        if (!force && Objects.equals(uiEntries, this.lastUiEntries)) return;

        this.lastUiEntries = uiEntries;

        ArrayList<CosmeticEntryRow> rows = new ArrayList<>(uiEntries.size());
        for (CosmeticUiEntry e : uiEntries) {
            if (e != null) rows.add(new CosmeticEntryRow(this, e));
        }

        if (this.list != null) {
            this.list.setEntries(List.copyOf(rows));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
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
        gg.drawString(this.font, this.tierLabel, left + PANEL_PAD, top + 10, 13421772);

        super.render(gg, mouseX, mouseY, partialTick);

        gg.fill(left + PANEL_PAD, subInt(top + HEADER_H, 2), subInt(right, PANEL_PAD), subInt(top + HEADER_H, 1), 1711276032);
        gg.fill(left + PANEL_PAD, top + HEADER_H + TABS_H, subInt(right, PANEL_PAD), top + HEADER_H + TABS_H + 1, 1711276032);
    }

    @Override
    public void onClose() {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null) {
            mc.setScreen(parent);
        }
    }

    private static int subInt(int a, int b) {
        return Math.addExact(a, Math.negateExact(b));
    }

    private static int computeContentWidth(int screenWidth) {
        int max = subInt(screenWidth, OUTER_MARGIN * 2);
        if (max > 520) max = 520;
        if (max < 300) max = 300;
        return max;
    }

    private static int computeContentHeight(int screenHeight) {
        int max = subInt(screenHeight, OUTER_MARGIN * 2);
        if (max > 340) max = 340;
        if (max < 240) max = 240;
        return max;
    }

    private void addTabs(int left, int y, int width) {
        CosmeticSlot[] values = CosmeticSlot.values();
        int count = values.length;
        if (count == 0) return;

        int base = width / count;
        int rem = width % count;

        int x = left;

        for (int i = 0; i < count; i++) {
            CosmeticSlot s = values[i];
            int w = base + (i < rem ? 1 : 0);

            Button tab = Button.builder(Component.literal(s.name()), new SwitchTabPress(parent, s))
                    .bounds(x, y, w, TABS_H)
                    .build();

            if (s == this.slot) tab.active = false;

            this.addRenderableWidget(tab);
            x += w;
        }
    }

    private void addFooter(int right, int bottom) {
        Button back = Button.builder(Component.literal("Back"), b -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) mc.setScreen(this.parent);
        }).bounds(subInt(right, 72), subInt(bottom, 22), 72, 20).build();

        this.addRenderableWidget(back);
    }

    private record SwitchTabPress(Screen parent, CosmeticSlot slot) implements Button.OnPress {
        @Override
        public void onPress(@NotNull Button button) {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) {
                mc.setScreen(new CosmeticSlotScreen(parent, slot));
            }
        }
    }

    private static final class CosmeticList extends ContainerObjectSelectionList<CosmeticEntryRow> {

        private final int leftPos;

        private CosmeticList(Minecraft mc, int width, int height, int top, int itemHeight, int leftPos) {
            super(mc, width, height, top, itemHeight);
            this.leftPos = leftPos;
        }

        public void setEntries(List<CosmeticEntryRow> entries) {
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

    private static final class CosmeticEntryRow extends ContainerObjectSelectionList.Entry<CosmeticEntryRow> {

        private final CosmeticSlotScreen screen;
        private final CosmeticUiEntry ui;
        private final Button equip;
        private final Button unequip;
        private final List<net.minecraft.client.gui.components.events.GuiEventListener> children;
        private final List<NarratableEntry> narratables;

        private CosmeticEntryRow(CosmeticSlotScreen screen, CosmeticUiEntry ui) {
            this.screen = screen;
            this.ui = ui;

            this.equip = Button.builder(Component.literal("Equip"), b -> {
                CosmeticEntry e = ui.entry();
                if (e != null) CosmeticUiActions.equip(e.id());
            }).bounds(0, 0, 58, 18).build();

            this.unequip = Button.builder(Component.literal("Clear"), b -> {
                CosmeticEntry e = ui.entry();
                if (e != null) CosmeticUiActions.unequip(e.slot());
            }).bounds(0, 0, 58, 18).build();

            this.children = List.of(this.equip, this.unequip);
            this.narratables = List.of(this.equip, this.unequip);
        }

        @Override
        public @NotNull List<? extends net.minecraft.client.gui.components.events.GuiEventListener> children() {
            return this.children;
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return this.narratables;
        }

        @Override
        public void render(
                @NotNull GuiGraphics gg,
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
            CosmeticEntry entry = ui.entry();
            if (entry == null || entry.info() == null) return;

            int pad = 6;
            int textX = x + pad;
            int textY = y + 7;

            gg.fill(x, y + 1, x + rowWidth, subInt(y + rowHeight, 1), hovered ? 872415231 : 603979776);
            gg.drawString(screen.font, Component.literal(entry.info().displayName()), textX, textY, 16777215);

            int btnX2 = subInt(x + rowWidth, 60);
            int btnX1 = subInt(btnX2, 60);
            int btnY = y + 4;

            this.equip.setX(btnX1);
            this.equip.setY(btnY);
            this.unequip.setX(btnX2);
            this.unequip.setY(btnY);

            boolean unlocked = ui.unlocked();
            boolean equipped = entry.equipped();

            this.equip.active = unlocked && !equipped;
            this.unequip.active = equipped;

            this.equip.render(gg, mouseX, mouseY, partialTick);
            this.unequip.render(gg, mouseX, mouseY, partialTick);

            if (!unlocked) {
                gg.drawString(screen.font, Component.literal("Locked T" + entry.info().requiredTier()), subInt(btnX1, 110), textY, 16733525);
            } else if (equipped) {
                gg.drawString(screen.font, Component.literal("On"), subInt(btnX1, 64), textY, 5635925);
            }
        }
    }
}