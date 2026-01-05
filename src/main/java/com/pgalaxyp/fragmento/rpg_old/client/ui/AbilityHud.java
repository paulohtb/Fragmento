package com.pgalaxyp.fragmento.rpg_old.client.ui;

import com.pgalaxyp.fragmento.rpg_old.client.ClientContext;
import com.pgalaxyp.fragmento.rpg_old.client.ClientNetworkProxy;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.AbilitySnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AbilityHud {

    private static final SkillSlotId NORMAL = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL = new SkillSlotId(2);

    private static final ItemStack READY = new ItemStack(Items.GREEN_CONCRETE);
    private static final ItemStack ACTIVE = new ItemStack(Items.BLUE_CONCRETE);
    private static final ItemStack COOLDOWN = new ItemStack(Items.RED_CONCRETE);

    public static void render(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.catalystActive(mc)) return;

        var snap = ClientNetworkProxy.snapshot();
        if (snap == null) return;

        AbilitySnapshot ab = snap.abilities();
        if (ab == null) return;

        Time now = ClientNetworkProxy.now();

        drawSlot(g, mc, 6, NORMAL, ab, now);
        drawSlot(g, mc, 30, SPECIAL, ab, now);
    }

    private static void drawSlot(
            GuiGraphics g,
            Minecraft mc,
            int x,
            SkillSlotId slot,
            AbilitySnapshot ab,
            Time now
    ) {
        SkillId skill = snapSkill(slot, ab);
        ItemStack icon = READY;

        if (ab.casting().containsKey(slot)) {
            icon = ACTIVE;
        }

        if (skill != null) {
            Time cd = ab.cooldownEndsAt().get(skill);
            if (cd != null && cd.isAfter(now)) {
                icon = COOLDOWN;
            }
        }

        int y = mc.getWindow().getGuiScaledHeight() - 22;
        g.renderItem(icon, x, y);
    }

    private static SkillId snapSkill(SkillSlotId slot, AbilitySnapshot ab) {
        SkillId armed = ab.infusedArmed().get(slot);
        if (armed != null) return armed;

        var snap = ClientNetworkProxy.snapshot();
        if (snap != null && snap.equippedSkills() != null) {
            return snap.equippedSkills().skillInSlot(slot);
        }

        return null;
    }

    private AbilityHud() {}
}