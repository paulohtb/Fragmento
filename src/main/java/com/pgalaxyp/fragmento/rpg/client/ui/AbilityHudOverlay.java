package com.pgalaxyp.fragmento.rpg.client.ui;

import com.pgalaxyp.fragmento.rpg.client.ClientContext;
import com.pgalaxyp.fragmento.rpg.client.ClientNetworkProxy;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.snapshot.AbilitySnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Locale;

public final class AbilityHudOverlay {

    private static final SkillSlotId NORMAL_SLOT = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL_SLOT = new SkillSlotId(2);

    private static final ItemStack RED = new ItemStack(Items.RED_CONCRETE);
    private static final ItemStack GREEN = new ItemStack(Items.GREEN_CONCRETE);
    private static final ItemStack BLUE = new ItemStack(Items.BLUE_CONCRETE);

    public static void render(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.inGame(mc)) return;
        if (!ClientContext.catalystActive(mc)) return;

        CombatSnapshot snap = ClientNetworkProxy.snapshot();
        if (snap == null) return;

        AbilitySnapshot ab = snap.abilities();
        if (ab == null) return;

        Time now = ClientNetworkProxy.now();
        if (now == null) return;

        int pad = 6;
        int yBase = mc.getWindow().getGuiScaledHeight() - pad - 16;

        int x1 = pad + 24;

        renderSlot(mc, g, pad, yBase, NORMAL_SLOT, ab, snap, now);
        renderSlot(mc, g, x1, yBase, SPECIAL_SLOT, ab, snap, now);
    }

    private static void renderSlot(
            Minecraft mc,
            GuiGraphics g,
            int x,
            int y,
            SkillSlotId slot,
            AbilitySnapshot ab,
            CombatSnapshot snap,
            Time now
    ) {
        SkillId skill = skillForSlot(ab, snap, slot);
        boolean armed = ab.infusedArmed().containsKey(slot);
        boolean casting = ab.casting().containsKey(slot);

        ItemStack base = GREEN;

        if (slot.index() == 1 && armed) {
            base = BLUE;
        }

        if (slot.index() == 2 && casting) {
            base = BLUE;
        }

        if (skill != null) {
            Time cd = ab.cooldownEndsAt().get(skill);
            if (cd != null && cd.isAfter(now)) {
                base = RED;
            }
        }

        g.renderItem(base, x, y);

        if (skill == null) return;

        Time cd = ab.cooldownEndsAt().get(skill);
        if (cd == null || !cd.isAfter(now)) return;

        long leftTicks = Math.max(0L, cd.ticks() - now.ticks());
        float secs = leftTicks / 20.0f;

        String txt = String.format(Locale.ROOT, "%.1f", secs);
        g.drawString(mc.font, txt, x + 18, y + 4, 0xFFFFFFFF, true);
    }

    private static SkillId skillForSlot(AbilitySnapshot ab, CombatSnapshot snap, SkillSlotId slot) {
        SkillId armed = ab.infusedArmed().get(slot);
        if (armed != null) return armed;

        if (snap.equippedSkills() != null) {
            return snap.equippedSkills().skillInSlot(slot);
        }

        return null;
    }

    private AbilityHudOverlay() {}
}