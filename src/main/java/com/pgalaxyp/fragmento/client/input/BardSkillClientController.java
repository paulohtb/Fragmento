package com.pgalaxyp.fragmento.client.input;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.core.util.RaycastUtil;
import com.pgalaxyp.fragmento.network.c2s.SkillIntentPacket;
import com.pgalaxyp.fragmento.system.skill.Skill;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public final class BardSkillClientController {

    private static long lastSendTick;

    private static final int CLIENT_SEND_INTERVAL_TICKS = 2;

    private BardSkillClientController() {
    }

    public static void reset() {
        lastSendTick = 0L;
    }

    public static void tick(
            Minecraft mc,
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument
    ) {
        if (mc == null || mc.level == null || player == null) {
            reset();
            return;
        }

        if (instrument == null) {
            return;
        }

        long now = player.tickCount;
        if (now < lastSendTick + CLIENT_SEND_INTERVAL_TICKS) {
            return;
        }

        if (CatalystKeybinds.NORMAL_USE != null && CatalystKeybinds.NORMAL_USE.consumeClick()) {
            sendIntent(player, stack, instrument, SkillSlot.BASIC);
            lastSendTick = now;
            return;
        }

        if (CatalystKeybinds.SPECIAL_USE != null && CatalystKeybinds.SPECIAL_USE.isDown()) {
            sendIntent(player, stack, instrument, SkillSlot.SPECIAL);
            lastSendTick = now;
        }
    }

    private static void sendIntent(
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument,
            SkillSlot slot
    ) {
        Skill skill = instrument.getSkill(slot);
        double range = skill != null ? skill.getRange(stack) : 12.0;

        RaycastUtil.Result rc = RaycastUtil.perform(player, range);

        int targetId = 0;
        if (rc.hasTarget()) {
            targetId = rc.target().getId();
        }

        PacketDistributor.sendToServer(
                new SkillIntentPacket(slot.id(), targetId)
        );
    }
}