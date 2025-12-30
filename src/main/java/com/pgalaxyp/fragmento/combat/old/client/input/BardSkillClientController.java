package com.pgalaxyp.fragmento.combat.old.client.input;

import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.combat.old.core.util.RaycastUtil;
import com.pgalaxyp.fragmento.combat.old.network.c2s.SkillCancelPacket;
import com.pgalaxyp.fragmento.combat.old.network.c2s.SkillIntentPacket;
import com.pgalaxyp.fragmento.combat.old.system.skill.Skill;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public final class BardSkillClientController {

    private static long lastSendTick;
    private static boolean specialWasDown;

    private static final int CLIENT_SEND_INTERVAL_TICKS = 2;

    private BardSkillClientController() {
    }

    public static void reset() {
        lastSendTick = 0L;
        specialWasDown = false;
    }

    public static void tick(
            Minecraft mc,
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument
    ) {
        if (!isReady(mc, player)) {
            reset();
            return;
        }

        if (instrument == null) {
            specialWasDown = false;
            return;
        }

        long now = player.tickCount;
        if (!canSend(now)) return;

        if (consumeNormalClick()) {
            sendStart(player, stack, instrument, SkillSlot.BASIC);
            markSent(now);
            return;
        }

        boolean specialDown = isSpecialDown();
        if (specialDown == specialWasDown) return;

        if (specialDown) {
            sendStart(player, stack, instrument, SkillSlot.SPECIAL);
        } else {
            sendCancel(SkillSlot.SPECIAL);
        }

        specialWasDown = specialDown;
        markSent(now);
    }

    private static boolean isReady(Minecraft mc, LocalPlayer player) {
        return mc != null && mc.level != null && player != null;
    }

    private static boolean canSend(long now) {
        return now >= lastSendTick + CLIENT_SEND_INTERVAL_TICKS;
    }

    private static void markSent(long now) {
        lastSendTick = now;
    }

    private static boolean consumeNormalClick() {
        return CatalystKeybinds.NORMAL_USE != null && CatalystKeybinds.NORMAL_USE.consumeClick();
    }

    private static boolean isSpecialDown() {
        return CatalystKeybinds.SPECIAL_USE != null && CatalystKeybinds.SPECIAL_USE.isDown();
    }

    private static void sendStart(
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument,
            SkillSlot slot
    ) {
        Skill skill = instrument.getSkill(slot);
        double range = skill != null ? skill.getRange(stack) : 12.0;

        int targetId = resolveClientTargetId(player, slot, range);

        PacketDistributor.sendToServer(
                new SkillIntentPacket(slot.id(), targetId)
        );
    }

    private static int resolveClientTargetId(LocalPlayer player, SkillSlot slot, double range) {
        RaycastUtil.Result rc = slot == SkillSlot.SPECIAL
                ? RaycastUtil.performPlayersOnly(player, range)
                : RaycastUtil.perform(player, range);

        if (!rc.hasTarget()) return 0;
        return rc.target().getId();
    }

    private static void sendCancel(SkillSlot slot) {
        PacketDistributor.sendToServer(
                new SkillCancelPacket(slot.id())
        );
    }
}