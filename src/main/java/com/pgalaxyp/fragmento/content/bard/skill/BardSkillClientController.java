package com.pgalaxyp.fragmento.content.bard.skill;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.core.util.RaycastUtil;
import com.pgalaxyp.fragmento.gameplay.skill.Skill;
import com.pgalaxyp.fragmento.gameplay.skill.SkillSlot;
import com.pgalaxyp.fragmento.platform.events.input.CatalystKeybinds;
import com.pgalaxyp.fragmento.platform.network.packet.SkillPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public final class BardSkillClientController {

    private static boolean specialChanneling;
    private static int specialCurrentTarget;

    private static long lastBasicSendTick;
    private static long lastSpecialSendTick;

    private static final int CLIENT_SEND_INTERVAL_TICKS = 2;

    private BardSkillClientController() {
    }

    public static void tick(
            Minecraft mc,
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument
    ) {
        if (instrument == null) {
            if (specialChanneling) cancelSpecial();
            return;
        }

        handleBasic(player, stack, instrument);
        handleSpecial(player, stack, instrument);
    }

    private static void handleBasic(
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument
    ) {
        if (CatalystKeybinds.NORMAL_USE == null) return;
        if (!CatalystKeybinds.NORMAL_USE.consumeClick()) return;

        if (stack.isEmpty()) return;
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return;

        long nowTick = player.tickCount;
        if (nowTick - lastBasicSendTick < CLIENT_SEND_INTERVAL_TICKS) return;
        lastBasicSendTick = nowTick;

        Skill skill = instrument.getSkill(SkillSlot.BASIC);
        double range = skill != null ? skill.getRange(stack) : 12.0;

        RaycastUtil.Result rc = RaycastUtil.perform(player, range);

        int target = 0;
        if (rc.hasTarget()) target = rc.target().getId();

        PacketDistributor.sendToServer(
                new SkillPacket(SkillPacket.Action.START, SkillSlot.BASIC.id(), target)
        );
    }

    private static void handleSpecial(
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument
    ) {
        if (CatalystKeybinds.SPECIAL_USE == null) return;

        if (stack.isEmpty()) {
            if (specialChanneling) cancelSpecial();
            return;
        }

        if (specialChanneling) {
            if (!CatalystKeybinds.SPECIAL_USE.isDown()) {
                cancelSpecial();
            }
            return;
        }

        if (!CatalystKeybinds.SPECIAL_USE.isDown()) return;
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return;

        long nowTick = player.tickCount;
        if (nowTick - lastSpecialSendTick < CLIENT_SEND_INTERVAL_TICKS) return;
        lastSpecialSendTick = nowTick;

        startSpecial(player, stack, instrument);
    }

    private static void startSpecial(
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument
    ) {
        Skill skill = instrument.getSkill(SkillSlot.SPECIAL);
        double range = skill != null ? skill.getRange(stack) : 12.0;

        RaycastUtil.Result rc = RaycastUtil.perform(player, range);

        int target = 0;
        if (rc.hasTarget() && rc.target() instanceof Player) {
            target = rc.target().getId();
        }

        specialCurrentTarget = target;
        specialChanneling = true;

        PacketDistributor.sendToServer(
                new SkillPacket(SkillPacket.Action.START, SkillSlot.SPECIAL.id(), target)
        );
    }

    private static void cancelSpecial() {
        PacketDistributor.sendToServer(
                new SkillPacket(SkillPacket.Action.CANCEL, SkillSlot.SPECIAL.id(), specialCurrentTarget)
        );

        specialChanneling = false;
        specialCurrentTarget = 0;
    }
}
