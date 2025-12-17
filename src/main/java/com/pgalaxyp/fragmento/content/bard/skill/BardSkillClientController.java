package com.pgalaxyp.fragmento.content.bard.skill;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.core.util.RaycastUtil;
import com.pgalaxyp.fragmento.system.skill.Skill;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import com.pgalaxyp.fragmento.client.input.CatalystKeybinds;
import com.pgalaxyp.fragmento.network.SkillPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

public final class BardSkillClientController {

    private static boolean specialChanneling;
    private static int specialCurrentTarget;

    private static long lastBasicSendTick;
    private static long lastSpecialSendTick;

    private static UUID lastPlayerUuid;

    private static final int CLIENT_SEND_INTERVAL_TICKS = 2;

    private BardSkillClientController() {
    }

    public static void resetClientState() {
        specialChanneling = false;
        specialCurrentTarget = 0;
        lastBasicSendTick = 0L;
        lastSpecialSendTick = 0L;
        lastPlayerUuid = null;
    }

    public static void tick(
            Minecraft mc,
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument
    ) {
        if (mc == null || mc.level == null || player == null) {
            resetClientState();
            return;
        }

        UUID id = player.getUUID();
        if (lastPlayerUuid == null || !lastPlayerUuid.equals(id)) {
            resetClientState();
            lastPlayerUuid = id;
        }

        long nowTick = player.tickCount;
        if (nowTick < lastBasicSendTick) lastBasicSendTick = 0L;
        if (nowTick < lastSpecialSendTick) lastSpecialSendTick = 0L;

        if (instrument == null) {
            if (specialChanneling) cancelSpecial();
            return;
        }

        handleBasic(player, stack, instrument, nowTick);
        handleSpecial(player, stack, instrument, nowTick);
    }

    private static void handleBasic(
            LocalPlayer player,
            ItemStack stack,
            BardCatalystItem instrument,
            long nowTick
    ) {
        if (CatalystKeybinds.NORMAL_USE == null) return;
        if (!CatalystKeybinds.NORMAL_USE.consumeClick()) return;

        if (stack.isEmpty()) return;
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return;

        if (nowTick < lastBasicSendTick + CLIENT_SEND_INTERVAL_TICKS) return;
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
            BardCatalystItem instrument,
            long nowTick
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

        if (nowTick < lastSpecialSendTick + CLIENT_SEND_INTERVAL_TICKS) return;
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
        if (rc.hasTarget() && rc.target() instanceof net.minecraft.world.entity.player.Player) {
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
