package com.pgalaxyp.fragmento.system.gameplay.cooldown;

import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerSkillCooldownService {

    private PlayerSkillCooldownService() {
    }

    public static boolean canUse(ServerPlayer player, SkillSlot slot) {
        if (player == null) return false;
        if (!(player.level() instanceof ServerLevel level)) return false;

        long now = level.getGameTime();
        return !PlayerSkillCooldownSavedData
                .get(level.getServer())
                .isOnCooldown(player.getUUID(), slot, now);
    }

    public static void apply(ServerPlayer player, SkillSlot slot, int ticks) {
        if (player == null) return;
        if (ticks <= 0) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        long end = level.getGameTime() + ticks;
        PlayerSkillCooldownSavedData
                .get(level.getServer())
                .apply(player.getUUID(), slot, end);
    }
}
