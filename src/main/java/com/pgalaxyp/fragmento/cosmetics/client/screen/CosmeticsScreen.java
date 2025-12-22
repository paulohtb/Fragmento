package com.pgalaxyp.fragmento.cosmetics.client.screen;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticCatalogEntry;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.client.CosmeticsClientRequests;
import com.pgalaxyp.fragmento.cosmetics.client.CosmeticsClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CosmeticsScreen extends Screen {

    private CosmeticSlot selectedSlot;
    private CosmeticsList list;
    private Button equipButton;
    private Button clearButton;
    private Button closeButton;
    private Button toggleOthersButton;

    private CosmeticCatalogEntry selectedEntry;

    public CosmeticsScreen() {
        super(Component.literal("Cosmetics"));
        this.selectedSlot = CosmeticSlot.HEAD;
    }

    @Override
    protected void init() {
        int left = Math.subtractExact(this.width / 2, 160);
        int top = Math.subtractExact(this.height / 2, 90);

        int listTop = Math.addExact(top, 30);
        int listHeight = 140;

        this.list = new CosmeticsList(this.minecraft, 220, listHeight, listTop);
        this.list.setLeftPos(left);
        this.addRenderableWidget(this.list);

        int slotX = left;
        int slotY = top;

        for (CosmeticSlot slot : CosmeticSlot.values()) {
            final CosmeticSlot slotFinal = slot;
            int w = 52;
            this.addRenderableWidget(
                    Button.builder(Component.literal(slotFinal.name()), new Button.OnPress() {
                        @Override
                        public void onPress(Button b) {
                            CosmeticsScreen.this.selectedSlot = slotFinal;
                            CosmeticsScreen.this.selectedEntry = null;
                            CosmeticsScreen.this.rebuildList();
                        }
                    }).bounds(slotX, slotY, w, 20).build()
            );
            slotX = Math.addExact(slotX, 54);
        }

        this.equipButton = this.addRenderableWidget(
                Button.builder(Component.literal("Equip"), new Button.OnPress() {
                    @Override
                    public void onPress(Button b) {
                        if (selectedEntry == null) return;
                        CosmeticId id = CosmeticId.of(selectedEntry.id());
                        CosmeticsClientRequests.requestSetBase(selectedSlot, id);
                    }
                }).bounds(Math.addExact(left, 228), Math.addExact(top, 30), 92, 20).build()
        );

        this.clearButton = this.addRenderableWidget(
                Button.builder(Component.literal("Clear"), new Button.OnPress() {
                            @Override
                            public void onPress(Button b) {
                                CosmeticsClientRequests.requestClearBase(selectedSlot);
                            }
                        })
                        .bounds(Math.addExact(left, 228), Math.addExact(top, 54), 92, 20).build()
        );

        this.toggleOthersButton = this.addRenderableWidget(
                Button.builder(toggleOthersLabel(), new Button.OnPress() {
                    @Override
                    public void onPress(Button b) {
                        boolean next = !CosmeticsClientState.showOthers();
                        CosmeticsClientState.setShowOthers(next);
                        b.setMessage(toggleOthersLabel());
                    }
                }).bounds(Math.addExact(left, 228), Math.addExact(top, 78), 92, 20).build()
        );

        this.closeButton = this.addRenderableWidget(
                Button.builder(Component.literal("Close"), new Button.OnPress() {
                            @Override
                            public void onPress(Button b) {
                                CosmeticsScreen.this.onClose();
                            }
                        })
                        .bounds(Math.addExact(left, 228), Math.addExact(top, 150), 92, 20).build()
        );

        rebuildList();
    }

    private Component toggleOthersLabel() {
        return Component.literal(CosmeticsClientState.showOthers() ? "Others On" : "Others Off");
    }

    private void rebuildList() {
        this.list.clearAllEntries();

        Map<CosmeticSlot, List<CosmeticCatalogEntry>> bySlot = CosmeticsClientState.unlockedBySlot();
        List<CosmeticCatalogEntry> unlocked = bySlot.get(selectedSlot);

        if (unlocked != null) {
            for (int i = 0; i < unlocked.size(); i++) {
                CosmeticCatalogEntry e = unlocked.get(i);
                this.list.addPublicEntry(new CosmeticsList.EntryUnlocked(this, e));
            }
        }

        CosmeticTier tier = CosmeticsClientState.localTier();
        int tierLevel = tier.level();
        int slotOrdinal = selectedSlot.ordinal();

        for (int t = Math.addExact(tierLevel, 1); t < 4; t++) {
            int count = CosmeticsClientState.lockedCount(t, slotOrdinal);
            for (int i = 0; i < count; i++) {
                this.list.addPublicEntry(new CosmeticsList.EntryLocked(this));
            }
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gg, mouseX, mouseY, partialTick);
        super.render(gg, mouseX, mouseY, partialTick);

        int left = Math.subtractExact(this.width / 2, 160);
        int top = Math.subtractExact(this.height / 2, 90);

        gg.drawString(this.font, this.title, left, Math.subtractExact(top, 12), 16777215);

        Minecraft mc = this.minecraft;
        UUID self = mc != null && mc.player != null ? mc.player.getUUID() : null;
        CosmeticLoadout loadout = self != null ? CosmeticsClientState.getEffective(self) : CosmeticLoadout.EMPTY;

        String equipped = "None";
        if (loadout != null) {
            CosmeticId id = loadout.get(selectedSlot);
            if (id != null) equipped = id.toString();
        }

        gg.drawString(this.font, Component.literal("Tier: " + CosmeticsClientState.localTier().name()), left, Math.addExact(top, 176), 14737632);
        gg.drawString(this.font, Component.literal("Equipped: " + equipped), left, Math.addExact(top, 188), 14737632);

        CosmeticsClientState.BaseSetResult r = CosmeticsClientState.lastSetBaseResult(selectedSlot.ordinal());
        String rs = "Last set: none";
        if (r != null) {
            rs = r.success() ? ("Last set: ok v" + r.version()) : ("Last set: fail " + r.errorCode() + " v" + r.version());
        }
        gg.drawString(this.font, Component.literal(rs), left, Math.addExact(top, 200), 14737632);

        String v1 = "Catalog v" + CosmeticsClientState.catalogDataVersion();
        gg.drawString(this.font, Component.literal(v1), left, Math.addExact(top, 212), 11184810);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }

    public void selectUnlocked(CosmeticCatalogEntry entry) {
        this.selectedEntry = entry;
    }

    private static final class CosmeticsList extends AbstractSelectionList<CosmeticsList.EntryBase> {

        private int leftPos;

        private CosmeticsList(Minecraft mc, int width, int height, int top) {
            super(mc, width, height, top, Math.addExact(top, height));
            this.leftPos = 0;
        }

        public void setLeftPos(int leftPos) {
            this.leftPos = leftPos;
        }

        @Override
        public int getRowLeft() {
            return leftPos;
        }

        @Override
        public int getRowWidth() {
            return this.width;
        }

        public void clearAllEntries() {
            this.clearEntries();
            this.setScrollAmount(0.0D);
        }

        public void addPublicEntry(EntryBase entry) {
            this.addEntry(entry);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
        }

        private abstract static class EntryBase extends AbstractSelectionList.Entry<EntryBase> {
        }

        private static final class EntryUnlocked extends EntryBase {

            private final CosmeticsScreen screen;
            private final CosmeticCatalogEntry entry;

            private EntryUnlocked(CosmeticsScreen screen, CosmeticCatalogEntry entry) {
                this.screen = screen;
                this.entry = entry;
            }

            @Override
            public void render(GuiGraphics gg, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
                String txt = entry.id().toString();
                gg.drawString(screen.font, Component.literal(txt), Math.addExact(x, 4), Math.addExact(y, 6), 16777215);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                screen.selectUnlocked(entry);
                return true;
            }
        }

        private static final class EntryLocked extends EntryBase {

            private final CosmeticsScreen screen;

            private EntryLocked(CosmeticsScreen screen) {
                this.screen = screen;
            }

            @Override
            public void render(GuiGraphics gg, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
                int x0 = Math.addExact(x, 2);
                int y0 = Math.addExact(y, 2);
                int x1 = Math.subtractExact(Math.addExact(x, width), 2);
                int y1 = Math.subtractExact(Math.addExact(y, height), 2);
                gg.fill(x0, y0, x1, y1, 0xFF000000);
                gg.drawString(screen.font, Component.literal("Locked"), Math.addExact(x, 6), Math.addExact(y, 6), 11184810);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                return true;
            }
        }
    }
}